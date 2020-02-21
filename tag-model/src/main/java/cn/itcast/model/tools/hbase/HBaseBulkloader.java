package cn.itcast.model.tools.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Spark HBase BulkLoad
 * spark-submit --class com.mengyao.tag.job.haier.HaierDataImport --master yarn --deploy-mode cluster --driver-memory 512m --executor-cores 1 --executor-memory 512m --queue default --verbose tags.jar
 *
 * @author mengyao
 */
public class HBaseBulkloader {

    private final static String appName = HBaseBulkloader.class.getSimpleName();
    private final static String HBASE_ZK_PORT_KEY = "hbase.zookeeper.property.clientPort";
    private final static String HBASE_ZK_QUORUM_KEY = "hbase.zookeeper.quorum";
    private final static String HBASE_ZK_PARENT_KEY = "zookeeper.znode.parent";
    private final static String DEFAULT_FS = "fs.defaultFS";
    private final static String DFS_REPLICATION = "dfs.replication";


    public static void main(String[] args) throws Exception {
        //args = new String[] {"1", "tbl_tag_user", "haier", "/apps/test/haier/user/tbl_user.csv", "/apps/test/haier/user/hfileout"};
        //args = new String[] {"2", "tbl_tag_product", "haier", "/apps/test/haier/product/tag_product.csv", "/apps/test/haier/product/hfileout"};
        //args = new String[] {"3", "tbl_tag_product_type", "haier", "/apps/test/haier/product_type/tag_product_type.csv", "/apps/test/haier/product_type/hfileout"};
        //args = new String[] {"4", "tbl_tag_order", "haier", "/apps/test/haier/order/tag_order.csv", "/apps/test/haier/order/hfileout"};
        //args = new String[] {"5", "tbl_tag_logs", "haier", "/apps/test/haier/log/tag_logs.csv", "/apps/test/haier/log/hfileout"};
        if (args.length < 5) {
            System.out.println("Usage: required params: <DataType> <HBaseTable> <Family> <InputDir> <OutputDir>");
            System.exit(-1);
        }
        //1User、2Product、3ProductType、4Order、5Log
        String dataType = args[0];
        //HBase Table
        String tableName = args[1];
        //HBase Table Family
        String family = args[2];
        //HBase Table Input RawData
        String inputDir = args[3];
        //HBase Table Input HFileData
        String outputDir = args[4];

        long start = System.currentTimeMillis();
        Configuration hadoopConf = HBaseConfiguration.create();
        hadoopConf.set(DEFAULT_FS, "hdfs://192.168.10.20:8020");
        hadoopConf.set(DFS_REPLICATION, "1");
        hadoopConf.set(HBASE_ZK_PORT_KEY, "2181");
        hadoopConf.set(HBASE_ZK_QUORUM_KEY, "192.168.10.20");
        hadoopConf.set(HBASE_ZK_PARENT_KEY, "/hbase-unsecure");
        hadoopConf.set(TableOutputFormat.OUTPUT_TABLE, tableName);

        Job job = Job.getInstance(hadoopConf, appName);
        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(KeyValue.class);
        job.setOutputFormatClass(HFileOutputFormat2.class);

        FileInputFormat.addInputPaths(job, inputDir);
        FileSystem fs = FileSystem.get(hadoopConf);
        Path output = new Path(outputDir);
        if (fs.exists(output)) {
            fs.delete(output, true);
        }
        fs.close();
        FileOutputFormat.setOutputPath(job, output);

        Connection connection = ConnectionFactory.createConnection(hadoopConf);
        TableName table = TableName.valueOf(tableName);
        HFileOutputFormat2.configureIncrementalLoad(job, connection.getTable(table), connection.getRegionLocator(table));

        SparkConf sparkConf = new SparkConf()
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .setMaster("local[*]")
                .setAppName(appName);

        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        jsc.textFile(inputDir)
                .persist(StorageLevel.MEMORY_AND_DISK_SER())
                .flatMapToPair(line -> extractText(Integer.valueOf(dataType), line, family).iterator())
                .coalesce(1)
                .sortByKey()//对key(ImmutableBytesWritable)做字典序排序
                .saveAsNewAPIHadoopFile(outputDir, ImmutableBytesWritable.class, KeyValue.class, HFileOutputFormat2.class, job.getConfiguration());

        LoadIncrementalHFiles load = new LoadIncrementalHFiles(hadoopConf);
        load.doBulkLoad(output, connection.getAdmin(), connection.getTable(table), connection.getRegionLocator(table));

        jsc.close();
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start) / 1000 + " 秒");
    }

    /**
     * @param type   1User、2Product、3ProductType、4Order、5Log
     * @param line
     * @param family
     * @return
     */
    static List<Tuple2<ImmutableBytesWritable, KeyValue>> extractText(int type, String line, String family) {
        if (type == 1) {//User
            return getLineToUser(line, family);
        }
        if (type == 2) {//Product
            return getLineToProduct(line, family);
        }
        if (type == 3) {//ProductType
            return getLineToProductType(line, family);
        }
        if (type == 4) {//Order
            return getLineToOrder(line, family);
        }
        if (type == 5) {//Log
            return getLineToLog(line, family);
        }
        return new ArrayList<Tuple2<ImmutableBytesWritable, KeyValue>>();
    }

    /**
     * 提取行为数据
     *
     * @param line
     * @param family
     * @return
     */
    private static List<Tuple2<ImmutableBytesWritable, KeyValue>> getLineToLog(String line, String family) {
        //使用TreeMap为qualifier做字典序排序
        TreeMap<String, Integer> fieldNames = new TreeMap<String, Integer>() {{
            put("id", 0);
            put("log_id", 1);
            put("remote_ip", 2);
            put("site_global_ticket", 3);
            put("site_global_session", 4);
            put("global_user_id", 5);
            put("cookie_text", 6);
            put("user_agent", 7);
            put("ref_url", 8);
            put("loc_url", 9);
            put("log_time", 10);
        }};
        List<Tuple2<ImmutableBytesWritable, KeyValue>> arr = new ArrayList<Tuple2<ImmutableBytesWritable, KeyValue>>();
        String[] fieldValues = line.split("\t", 11);
        //System.out.println("内容："+line+"\n字段数："+fieldValues.length);
        if (fieldValues != null && fieldValues.length == 11) {
            String id = fieldValues[0];
            byte[] rowkey = Bytes.toBytes(id);
            byte[] columnFamily = Bytes.toBytes(family);
            ImmutableBytesWritable ibw = new ImmutableBytesWritable(rowkey);
            fieldNames.forEach((k, v) -> {
                arr.add(new Tuple2<>(ibw, new KeyValue(rowkey, columnFamily, Bytes.toBytes(k), Bytes.toBytes(fieldValues[v]))));
            });
        }
        return arr;
    }

    /**
     * 提取订单数据
     *
     * @param line
     * @param family
     * @return
     */
    private static List<Tuple2<ImmutableBytesWritable, KeyValue>> getLineToOrder(String line, String family) {
        //使用TreeMap为qualifier做字典序排序
        TreeMap<String, Integer> fieldNames = new TreeMap<String, Integer>() {{
            put("id", 0);
            put("siteId", 1);
            put("isTest", 2);
            put("hasSync", 3);
            put("isBackend", 4);
            put("isCod", 5);
            put("notAutoConfirm", 6);
            put("orderSn", 7);
            put("relationOrderSn", 8);
            put("memberId", 9);
            put("productId", 10);
            put("memberEmail", 11);
            put("addTime", 12);
            put("syncTime", 13);
            put("orderStatus", 14);
            put("payTime", 15);
            put("paymentStatus", 16);
            put("receiptConsignee", 17);
            put("receiptAddress", 18);
            put("receiptZipcode", 19);
            put("receiptMobile", 20);
            put("productAmount", 21);
            put("orderAmount", 22);
            put("paidBalance", 23);
            put("giftCardAmount", 24);
            put("paidAmount", 25);
            put("shippingAmount", 26);
            put("totalEsAmount", 27);
            put("usedCustomerBalanceAmount", 28);
            put("customerId", 29);
            put("bestShippingTime", 30);
            put("paymentCode", 31);
            put("payBankCode", 32);
            put("paymentName", 33);
            put("consignee", 34);
            put("originRegionName", 35);
            put("originAddress", 36);
            put("province", 37);
            put("city", 38);
            put("region", 39);
            put("street", 40);
            put("markBuilding", 41);
            put("poiId", 42);
            put("poiName", 43);
            put("regionName", 44);
            put("address", 45);
            put("zipcode", 46);
            put("mobile", 47);
            put("phone", 48);
            put("receiptInfo", 49);
            put("delayShipTime", 50);
            put("remark", 51);
            put("bankCode", 52);
            put("agent", 53);
            put("confirmTime", 54);
            put("firstConfirmTime", 55);
            put("firstConfirmPerson", 56);
            put("finishTime", 57);
            put("tradeSn", 58);
            put("signCode", 59);
            put("source", 60);
            put("sourceOrderSn", 61);
            put("onedayLimit", 62);
            put("logisticsManner", 63);
            put("afterSaleManner", 64);
            put("personManner", 65);
            put("visitRemark", 66);
            put("visitTime", 67);
            put("visitPerson", 68);
            put("sellPeople", 69);
            put("sellPeopleManner", 70);
            put("orderType", 71);
            put("hasReadTaobaoOrderComment", 72);
            put("memberInvoiceId", 73);
            put("taobaoGroupId", 74);
            put("tradeType", 75);
            put("stepTradeStatus", 76);
            put("stepPaidFee", 77);
            put("depositAmount", 78);
            put("balanceAmount", 79);
            put("autoCancelDays", 80);
            put("isNoLimitStockOrder", 81);
            put("ccbOrderReceivedLogId", 82);
            put("ip", 83);
            put("isGiftCardOrder", 84);
            put("giftCardDownloadPassword", 85);
            put("giftCardFindMobile", 86);
            put("autoConfirmNum", 87);
            put("codConfirmPerson", 88);
            put("codConfirmTime", 89);
            put("codConfirmRemark", 90);
            put("codConfirmState", 91);
            put("paymentNoticeUrl", 92);
            put("addressLon", 93);
            put("addressLat", 94);
            put("smConfirmStatus", 95);
            put("smConfirmTime", 96);
            put("smManualTime", 97);
            put("smManualRemark", 98);
            put("isTogether", 99);
            put("isNotConfirm", 100);
            put("tailPayTime", 101);
            put("points", 102);
            put("modified", 103);
            put("channelId", 104);
            put("isProduceDaily", 105);
            put("couponCode", 106);
            put("couponCodeValue", 107);
            put("ckCode", 108);

        }};
        List<Tuple2<ImmutableBytesWritable, KeyValue>> arr = new ArrayList<Tuple2<ImmutableBytesWritable, KeyValue>>();
        String[] fieldValues = line.split("\t", 109);
        System.out.println("内容：" + line + "\n字段数：" + fieldValues.length);
        if (fieldValues != null && fieldValues.length == 109) {
            String id = fieldValues[0];
            byte[] rowkey = Bytes.toBytes(id);
            byte[] columnFamily = Bytes.toBytes(family);
            ImmutableBytesWritable ibw = new ImmutableBytesWritable(rowkey);
            fieldNames.forEach((k, v) -> {
                arr.add(new Tuple2<>(ibw, new KeyValue(rowkey, columnFamily, Bytes.toBytes(k), Bytes.toBytes(fieldValues[v]))));
            });
        }
        return arr;
    }

    /**
     * 提取商品品类数据
     *
     * @param line
     * @param family
     * @return
     */
    private static List<Tuple2<ImmutableBytesWritable, KeyValue>> getLineToProductType(String line, String family) {
        //使用TreeMap为qualifier做字典序排序
        TreeMap<String, Integer> fieldNames = new TreeMap<String, Integer>() {{
            put("id", 0);
            put("name", 1);
            put("level", 2);
            put("pid", 3);
            put("ctime", 4);
            put("utime", 5);
            put("remark", 6);
        }};
        List<Tuple2<ImmutableBytesWritable, KeyValue>> arr = new ArrayList<Tuple2<ImmutableBytesWritable, KeyValue>>();
        String[] fieldValues = line.split("\t", 7);
        //System.out.println("内容："+line+"\n字段数："+fieldValues.length);
        if (fieldValues != null && fieldValues.length == 7) {
            String id = fieldValues[0];
            byte[] rowkey = Bytes.toBytes(id);
            byte[] columnFamily = Bytes.toBytes(family);
            ImmutableBytesWritable ibw = new ImmutableBytesWritable(rowkey);
            fieldNames.forEach((k, v) -> {
                arr.add(new Tuple2<>(ibw, new KeyValue(rowkey, columnFamily, Bytes.toBytes(k), Bytes.toBytes(fieldValues[v]))));
            });
        }
        return arr;
    }

    /**
     * 提取用户数据
     *
     * @param line
     * @param family
     * @return
     */
    static List<Tuple2<ImmutableBytesWritable, KeyValue>> getLineToUser(String line, String family) {
        //使用TreeMap为qualifier做字典序排序
        TreeMap<String, Integer> fieldNames = new TreeMap<String, Integer>() {
            {
                put("id", 0);
                put("site_id", 1);
                put("email", 2);
                put("username", 3);
                put("password", 4);
                put("salt", 5);
                put("reg_time", 6);
                put("last_login_time", 7);
                put("last_login_ip", 8);
                put("member_rank_id", 9);
                put("big_customer_id", 10);
                put("last_putress_id", 11);
                put("last_payment_code", 12);
                put("gender", 13);
                put("birthday", 14);
                put("qq", 15);
                put("msn", 16);
                put("mobile", 17);
                put("can_receive_sms", 18);
                put("phone", 19);
                put("valid_code", 20);
                put("pwd_err_count", 21);
                put("source", 22);
                put("sign", 23);
                put("money", 24);
                put("money_pwd", 25);
                put("is_email_verify", 26);
                put("is_sms_verify", 27);
                put("sms_verify_code", 28);
                put("email_verify_code", 29);
                put("verify_send_coupon", 30);
                put("can_receive_email", 31);
                put("modified", 32);
                put("channel_id", 33);
                put("grade_id", 34);
                put("nick_name", 35);
                put("is_black_list", 36);
            }
        };
        List<Tuple2<ImmutableBytesWritable, KeyValue>> arr = new ArrayList<Tuple2<ImmutableBytesWritable, KeyValue>>();
        String[] fieldValues = line.split("\t", 37);
        //System.out.println("内容："+line+"\n字段数："+fieldValues.length);
        if (fieldValues != null && fieldValues.length == 37) {
            String id = fieldValues[0];
            byte[] rowkey = Bytes.toBytes(id);
            byte[] columnFamily = Bytes.toBytes(family);
            ImmutableBytesWritable ibw = new ImmutableBytesWritable(rowkey);
            fieldNames.forEach((k, v) -> {
                arr.add(new Tuple2<>(ibw, new KeyValue(rowkey, columnFamily, Bytes.toBytes(k), Bytes.toBytes(fieldValues[v]))));
            });
        }
        return arr;
    }

    /**
     * 提取商品数据
     *
     * @param line
     * @param family
     * @return
     */
    static List<Tuple2<ImmutableBytesWritable, KeyValue>> getLineToProduct(String line, String family) {
        //使用TreeMap为qualifier做字典序排序
        TreeMap<String, Integer> fieldNames = new TreeMap<String, Integer>() {{
            put("id", 0);
            put("name", 1);
            put("title", 2);
            put("pmode", 3);
            put("price", 4);
            put("discount_price", 5);
            put("retail_mode", 6);
            put("state", 7);
            put("ctime", 8);
            put("utime", 9);
            put("p_LeiBie", 10);
            put("p_JingZhong_MaoZhong", 11);
            put("p_YanShiYuYue", 12);
            put("p_WaiGuan", 13);
            put("p_WaiJiChiCun", 14);
            put("p_KuanShi", 15);
            put("p_EDingReFuHe", 16);
            put("p_ShuMaXianShi", 17);
            put("p_LianJieFangShi", 18);
            put("p_KuanGaoHouHanDiZuo", 19);
            put("p_XuNiQiangShuRuDianYa_DianLiu", 20);
            put("p_QiYuanLeiBie", 21);
            put("p_ZaoShengDb", 22);
            put("p_ShiYongHuanJingLeiXing", 23);
            put("p_FaPaoJi", 24);
            put("p_JieShuiJiShu", 25);
            put("p_EDianJiYi", 26);
            put("p_4In1KaTongXiangChiCun", 27);
            put("p_XianShiFangShi", 28);
            put("p_ZhuangPeiRuanGuanNeiJing", 29);
            put("p_ShiYang", 30);
            put("p_ShangShiShiJian", 31);
            put("p_ZaoYinDb", 32);
            put("p_MianBanCaiZhi", 33);
            put("p_CaiZhi", 34);
            put("p_ZiDongYinLiangKongZhi", 35);
            put("p_XingZouSuDong", 36);
            put("p_ShengYuReShuiLiangXianShi", 37);
            put("p_UsbzhiChiShiPinGeShi", 38);
            put("p_QingSaoShiJian", 39);
            put("p_150DKaiMen", 40);
            put("p_ShangGaiYanSe", 41);
            put("p_HeiXiaZiKongZhiJiShu", 42);
            put("p_WaiJiZaoYin", 43);
            put("p_EDingDianYa_PinLv", 44);
            put("p_WuBianJieXianShi", 45);
            put("p_JiaReFangShi", 46);
            put("p_ZhuJiChiCun", 47);
            put("p_DianChiLeiXing", 48);
            put("p_ZhiNengTouFang", 49);
            put("p_UsbzhiChiTuPianGeShi", 50);
            put("p_QiDongShuiLiuLiang", 51);
            put("p_ZhiNengJingHua", 52);
            put("p_V6ZhengQiTang", 53);
            put("p_ChanPinXingHao", 54);
            put("p_NeiBuCunChuKongJian", 55);
            put("p_Hdmi", 56);
            put("p_UsbzhiChiYinPinGeShi", 57);
            put("p_ZhongWenBaoWen", 58);
            put("p_KuaMenJianGaoDong", 59);
            put("p_ZhengJiGongLv", 60);
            put("p_EDingShuRuGongLv", 61);
            put("p_KeXuanShuiWei", 62);
            put("p_ZhengQiChiXuShiJian", 63);
            put("p_JueYuanBaoHu", 64);
            put("p_ShuiXiangRongJi", 65);
            put("p_ShuZiDianShi", 66);
            put("p_JinFengFangShi", 67);
            put("p_LengDongShiRongJi", 68);
            put("p_ZhiLengLiang", 69);
            put("p_ShuaTouFuJian", 70);
            put("p_NeiTongCaiZhi", 71);
            put("p_LanHuoMiaoZhuanLiAnQuanJiShu", 72);
            put("p_ShuangDianCiBiLiFa", 73);
            put("p_ChongDianShiJian", 74);
            put("p_DianChiChongDianDianYa", 75);
            put("p_JingYin", 76);
            put("p_WaiBaoZhuangXiangChiCun", 77);
            put("p_AnJianFangShi", 78);
            put("p_ShiFouYuYue", 79);
            put("p_WaiXingChiCun", 80);
            put("p_ZhiGuoJiaCaiZhi", 81);
            put("p_YiGanJiTing", 82);
            put("p_YouWuXiHuoBaoHu", 83);
            put("p_HaoDianLiang", 84);
            put("p_DianChiShuChuDianYa", 85);
            put("p_PingHuaLcdxianPing", 86);
            put("p_ZongRongJi", 87);
            put("p_SongFengFangShi", 88);
            put("p_ZaoYinZhi", 89);
            put("p_ZhiNeng", 90);
            put("p_ZiDongChuShuang", 91);
            put("p_XiYouYanJiWaiXingChiCun", 92);
            put("p_TeSeGongNeng", 93);
            put("p_ShuiXiangRongLiang", 94);
            put("p_LedkongZhiMianBan", 95);
            put("p_ChaoYuanJuLiSongFeng", 96);
            put("p_EDingYaLi", 97);
            put("p_DianChiRongLiang", 98);
            put("p_WangLuo", 99);
            put("p_ZhiReLiang", 100);
            put("p_FaReQiCaiZhi", 101);
            put("p_LengDongXingJi", 102);
            put("p_QiangLiQuWu", 103);
            put("p_EDingZhiReDianLiu", 104);
            put("p_RanQiZaoJingZhong", 105);
            put("p_BaoZhuangZhongLiang", 106);
            put("p_FangDianQiangJiShu", 107);
            put("p_ZhuanLiJinGangSanCengDan", 108);
            put("p_ShuZiYiTi", 109);
            put("p_ShiWaiJiMaoZhiLiang", 110);
            put("p_XiangTiCaiZhi", 111);
            put("p_EPingHuDong", 112);
            put("p_SuiJiFuJian", 113);
            put("p_BaoZhuangXiangChiCun", 114);
            put("p_YaoKongQi", 115);
            put("p_DianFuJiaRe", 116);
            put("p_EXiaShuangXi", 117);
            put("p_AvshuChu", 118);
            put("p_AnQuanYuJingJiShu", 119);
            put("p_FangGanShaoBaoHu", 120);
            put("p_ChuRe_SuReDongHeYiJiShu", 121);
            put("p_YuYue", 122);
            put("p_DianJiLeiXing", 123);
            put("p_WaiJiBaoZhuangXiangChiCun", 124);
            put("p_XuNiQiangHongWaiFaSheQiangDong_YouXiaoJuLi", 125);
            put("p_ZiBianCheng", 126);
            put("p_ZhiNengSeYuYanShen", 127);
            put("p_ChangKuanGao_BuHanDiZuo", 128);
            put("p_JingZhong", 129);
            put("p_JiYanQiangCaiZhi", 130);
            put("p_HuiChongShiJian", 131);
            put("p_JiChenHeRongJi", 132);
            put("p_NengHaoDengJi", 133);
            put("p_RanQiZaoZhuangPeiRuanGuanNeiJing", 134);
            put("p_ZhaoMingDengGongLv", 135);
            put("p_WaiJiDaiBaoZhuangXiangDeChiCun", 136);
            put("p_ShuoMingShu", 137);
            put("p_EDingChanReShuiNengLi", 138);
            put("p_YunXingZaoYin", 139);
            put("p_GongJinShu", 140);
            put("p_ZiDongHuaChengDong", 141);
            put("p_HongGanRongLiang", 142);
            put("p_ShiNeiJiJingZhiLiang", 143);
            put("p_PaiShuiKouWeiZhi", 144);
            put("p_TongSuoGongNeng", 145);
            put("p_NeiJiDaKongChiCun", 146);
            put("p_RfshePinDongZi", 147);
            put("p_ZhiLengLeiXing", 148);
            put("p_XiangTiYanSe", 149);
            put("p_CengJiaZaiWuZhongLiang", 150);
            put("p_ChaoWenBaoHu", 151);
            put("p_ChanPinJingZhong", 152);
            put("p_BaoZhuangChiCun", 153);
            put("p_ZhiMiSuoWenQiang", 154);
            put("p_RanQiZaoWaiXingChiCun", 155);
            put("p_DianFuJiaReGongLv", 156);
            put("p_FengLiang", 157);
            put("p_RanQiZaoWaKongChiCun", 158);
            put("p_ChanPinWaiGuanChiCun", 159);
            put("p_PiaoShuaiDongHeYi", 160);
            put("p_QiangLi", 161);
            put("p_LanYa", 162);
            put("p_ZuiGaoWenDong", 163);
            put("p_ChangKuanGao_HanDiZuo", 164);
            put("p_MaoZhong", 165);
            put("p_TongGanZao", 166);
            put("p_KongQiKaiGuan", 167);
            put("p_MaoZhongJingZhong", 168);
            put("p_KuanGaoHouBuHanDiZuo", 169);
            put("p_ZiQingJie", 170);
            put("p_WangLuoLianJie", 171);
            put("p_PaiShuiFangShi", 172);
            put("p_XianChang", 173);
            put("p_LuoJiChiCun", 174);
            put("p_ZhongTuTianYi", 175);
            put("p_XiJingJiTing", 176);
            put("p_DiYaRanShaoBaoHu", 177);
            put("p_Deng", 178);
            put("p_ZhuJiZuiDaGongLv", 179);
            put("p_DianYaFanWei", 180);
            put("p_ZhiChengGanGaoDong", 181);
            put("p_XiLie", 182);
            put("p_TongSuo", 183);
            put("p_FangZhouJinPao", 184);
            put("p_DangWei", 185);
            put("p_XiangTiCaiLiao", 186);
            put("p_ShuZiDianShiJieKou", 187);
            put("p_FengJiZhuanSu", 188);
            put("p_TuoShuiGongLv", 189);
            put("p_LiuMeiTiKaCaoJieKou", 190);
            put("p_JiaReGongLv", 191);
            put("p_NeiJiDaiBaoZhuangXiangDeChiCun", 192);
            put("p_ZhiLengFangShi", 193);
            put("p_TuoShuiRongLiang", 194);
            put("p_GuoCaiHe", 195);
            put("p_1LiFangMiRongNaTaiShu", 196);
            put("p_WaiXiangMaoZhong", 197);
            put("p_PingZuo", 198);
            put("p_BingXiangXingHao", 199);
            put("p_ShiYongShuiYa", 200);
            put("p_ZhuangXiangDan", 201);
            put("p_ShiWaiJiJingZhiLiang", 202);
            put("p_GuFengZengYangJiShu", 203);
            put("p_XiHuoBaoHu", 204);
            put("p_LengDongChouTi", 205);
            put("p_MaxxbasspingBanZhongDiYin", 206);
            put("p_DianYa", 207);
            put("p_DianHuoFangShi", 208);
            put("p_ZhongLiang", 209);
            put("p_CohanLiang", 210);
            put("p_Wifi", 211);
            put("p_ZaoYinZhiShu", 212);
            put("p_NengXiaoDengJi", 213);
            put("p_UsbjieKou", 214);
            put("p_JianKangChenYu", 215);
            put("p_EDingGongLv", 216);
            put("p_ZhengQiDaoGuanCaiZhi", 217);
            put("p_YeXi", 218);
            put("p_EZhongShengYinMoShi", 219);
            put("p_EDingShuChuGongLv", 220);
            put("p_BianWenChouTi", 221);
            put("p_MianBanYanSe", 222);
            put("p_XiZhenKongDai", 223);
            put("p_Eer", 224);
            put("p_YunTangMianBanCaiZhi", 225);
            put("p_YaSuoJiLeiXing", 226);
            put("p_IidaiLanHuoMiaoZhuanLiAnQuanJiShu", 227);
            put("p_YouXiaoRongJi", 228);
            put("p_ZhuJiJingZhong", 229);
            put("p_EDingPinLv", 230);
            put("p_ZuiGaoTuoShuiZhuanSu", 231);
            put("p_YanSe", 232);
            put("p_PeiJian", 233);
            put("p_FangGanShao", 234);
            put("p_BeiGuangYuan", 235);
            put("p_YinLaSiMianBan", 236);
            put("p_RanShaoXiTong", 237);
            put("p_Ypbpr", 238);
            put("p_ZiDongYinLiangXianZhi", 239);
            put("p_QiYuan", 240);
            put("p_ChuJiaQuan", 241);
            put("p_RongJi", 242);
            put("p_ZhuJiDongDingGongLv", 243);
            put("p_EDingDianYa", 244);
            put("p_NeiJiZaoYinDb", 245);
            put("p_ZhiGuoJiaXingZhuang", 246);
            put("p_ELiChuShi", 247);
            put("p_JiaGeQuJian", 248);
            put("p_WaiBaoZhuangChiCun", 249);
            put("p_BaoZhuangFangShi", 250);
            put("p_ZhunShiYuYue", 251);
            put("p_ChenRongLiang", 252);
            put("p_NengXiaoBiaoShiDengJi", 253);
            put("p_DianBiaoYaoQiu", 254);
            put("p_1In1KaTongXiangChiCun", 255);
            put("p_GaoChunDongWuYangTongShuiXiang", 256);
            put("p_PaiYanFangShi", 257);
            put("p_ZhenKongXiLiZhi", 258);
            put("p_ZhiNengCaoKong", 259);
            put("p_Cpu", 260);
            put("p_BaoZhuangQingDan", 261);
            put("p_XiDiRongLiang", 262);
            put("p_ZhuangPingShu", 263);
            put("p_TuXiangZhiShi", 264);
            put("p_ZuiDaFengYa", 265);
            put("p_JiChenFangShi", 266);
            put("p_XunHuanFengLiang", 267);
            put("p_LengCangShiRongJi", 268);
            put("p_DanHe", 269);
            put("p_WaiXiangChiCun", 270);
            put("p_EZhongBanYinMoShi", 271);
            put("p_ZhuanSu", 272);
            put("p_ChanPinLeiXing", 273);
            put("p_EDingZhiLengGongLv", 274);
            put("p_RanQiZaoDongDingYaLi", 275);
            put("p_BianWenShiRongJi", 276);
            put("p_YiJianZiQingJie", 277);
            put("p_RanQiZaoXingHao", 278);
            put("p_DianYuanXianChangDong", 279);
            put("p_45DkaiMen", 280);
            put("p_DianYuanZhongLei", 281);
            put("p_ShuiGuanKeYinCangAnZhuang", 282);
            put("p_ShiYongMianJi", 283);
            put("p_JingZhongBuHanDiZuo", 284);
            put("p_XiDiChengXu", 285);
            put("p_AnZhuangFangShi", 286);
            put("p_TingDianBuChang", 287);
            put("p_ZhuTiCaiZhi_WaiZhuangShiZhaoBuWei", 288);
            put("p_XiaoDongXingJi", 289);
            put("p_JiChenRongLiang", 290);
            put("p_LcdyeJingXianShi", 291);
            put("p_JingZhongHanDiZuo", 292);
            put("p_LiuMeiTi", 293);
            put("p_ZhiChengGanCaiZhi", 294);
            put("p_HongWaiXianYaoKongJiShu", 295);
            put("p_DanLiJinNeiTong", 296);
            put("p_ZaoJuYanShu", 297);
            put("p_KongZhiFangShi", 298);
            put("p_GeWuJia", 299);
            put("p_SuDongGongNeng", 300);
            put("p_PingMuChiCun", 301);
            put("p_ZhiNengFengYaJiShu", 302);
            put("p_ZiDongDang", 303);
            put("p_EDingZhiReGongLv", 304);
            put("p_FengGe", 305);
            put("p_DiZuopeiZhi", 306);
            put("p_BingXiangLengGuiJiXing", 307);
            put("p_ZhiNengHuYan", 308);
            put("p_LedyeJingXianShi", 309);
            put("p_AvliTiYin", 310);
            put("p_GongLv", 311);
            put("p_ZuiJiaGuanKanJuLi", 312);
            put("p_DingPin_BianPin", 313);
            put("p_ChaoDaGaoChunDongMeiBang", 314);
            put("p_KuanPinShuiWenDiaoJie", 315);
            put("p_ZhuJiCaiLiao", 316);
            put("p_PingMuBiLi", 317);
            put("p_XiYouYanJiJingZhong", 318);
            put("p_QiHouLeiXing", 319);
            put("p_FenBianLv", 320);
            put("p_AnZhuangWeiZhi", 321);
            put("p_ZhiLengJi", 322);
            put("p_ZhuJiChiCunZhiJing_Gao", 323);
            put("p_AvshuRu", 324);
            put("p_ShuRuGongLv", 325);
            put("p_XiDiGongLv", 326);
            put("p_XingHao", 327);
            put("p_NeiXiangChiCun", 328);
            put("p_QiangLiMoShi", 329);
            put("p_ShiNeiJiMaoZhiLiang", 330);
            put("p_JiuDongSeYuYanShen", 331);
            put("p_EJiDongZi", 332);
            put("p_GuoLvFangShi", 333);
            put("p_RanQiZaoMaoZhong", 334);
            put("p_WangLuoJieKou", 335);
            put("p_PinPai", 336);
            put("p_ZhengQiLiang", 337);
            put("p_LengMei", 338);
            put("p_HuiChongZuoShuChuDianYa_DianLiu", 339);
            put("p_MoBuChiCun", 340);
            put("p_NeiJiZaoYin", 341);
            put("p_ShangShiRiQi", 342);
            put("p_YingGeLaiBuXiuGangJiaReGuan", 343);
            put("p_YiLiangZiXuan", 344);
            put("p_NeiJiChiCun", 345);
            put("p_NeiXiangMaoZhong", 346);
            put("p_ZaoMianCaiZhi", 347);
            put("p_EDongHuoLiZiDongQieHuan", 348);
            put("p_WaKongChiCun", 349);
            put("p_JunHengQi", 350);
            put("p_ChanPinChiCun", 351);
            put("p_XiLiZhi", 352);
            put("p_KaiMenBaoJing", 353);
            put("p_ChiCun", 354);
            put("p_ZhenKongDong", 355);
            put("p_NeiJiBaoZhuangXiangChiCun", 356);
            put("p_ZaoYin", 357);
            put("p_EDingZhiLengDianLiu", 358);
            put("p_HuiChongZuoShuRuDianYa_DianLiu", 359);
            put("p_Pmv", 360);
            put("p_CaoZuoXiTong", 361);
            put("p_Apf", 362);
            put("p_ZhiNengWangLuo", 363);
            put("p_ZhengTiMaoZhong", 364);
            put("p_PiShu", 365);
            put("p_WaiGuanKuanShi", 366);
            put("p_YouWang", 367);
            put("p_XiYouYanJiYouZhiFenLiDong", 368);
            put("p_ChanPinPinLei", 369);
            put("p_LeiXing", 370);
        }};
        List<Tuple2<ImmutableBytesWritable, KeyValue>> arr = new ArrayList<Tuple2<ImmutableBytesWritable, KeyValue>>();
        String[] fieldValues = line.split("\t", 371);
        //System.out.println("内容："+line+"\n字段数："+fieldValues.length);
        if (fieldValues != null && fieldValues.length == 371) {
            String id = fieldValues[0];
            byte[] rowkey = Bytes.toBytes(id);
            byte[] columnFamily = Bytes.toBytes(family);
            ImmutableBytesWritable ibw = new ImmutableBytesWritable(rowkey);
            fieldNames.forEach((k, v) -> {
                arr.add(new Tuple2<>(ibw, new KeyValue(rowkey, columnFamily, Bytes.toBytes(k), Bytes.toBytes(fieldValues[v]))));
            });
        }
        return arr;
    }

}