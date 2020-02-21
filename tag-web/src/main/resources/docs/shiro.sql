
DROP TABLE IF EXISTS `tbl_user`;

CREATE TABLE `tbl_user` (
  `id`              bigint(32)   unsigned NOT NULL AUTO_INCREMENT,
  `username`        varchar(32)  COMMENT '登录账号',
  `password`        varchar(32)  COMMENT '登录密码',
  `real_name`       varchar(32)  COMMENT '真实姓名',
  `nid`             varchar(32)  COMMENT '身份证号:National ID number',
  `phone`           varchar(16)  COMMENT '手机号',
  `email`           varchar(32)  COMMENT '邮箱',
  
  `is_active`       tinyint(8) unsigned DEFAULT 1 COMMENT '是否有效/启用',
  `is_deleted`      tinyint(8) unsigned DEFAULT 0 COMMENT '是否逻辑删除',
  `user_create`     bigint(32)   unsigned COMMENT '创建人',
  `user_modified`   bigint(32)   unsigned COMMENT '修改人',
  `ctime`           datetime COMMENT '创建时间',
  `utime`           datetime COMMENT '修改时间',
  `remark`            varchar(255) COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户表';

DROP TABLE IF EXISTS `tbl_group`;

CREATE TABLE `tbl_group` (
  `id`              bigint(32) unsigned NOT NULL AUTO_INCREMENT,
  `p_id`            bigint(32) unsigned COMMENT '父级组织', 
  `name`            varchar(32) COMMENT '组织名称',
  `level`           tinyint(8) unsigned DEFAULT 1 COMMENT '组织级别（最大255）',
  
  `is_active`       tinyint(8) unsigned DEFAULT 1 COMMENT '是否有效/启用',
  `is_deleted`      tinyint(8) unsigned DEFAULT 0 COMMENT '是否逻辑删除',
  `user_create`     bigint(32)   unsigned COMMENT '创建人',
  `user_modified`   bigint(32)   unsigned COMMENT '修改人',
  `ctime`           datetime COMMENT '创建时间',
  `utime`           datetime COMMENT '修改时间',
  `remark`            varchar(255) COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织表';

DROP TABLE IF EXISTS `tbl_role`;

CREATE TABLE `tbl_role` (
  `id`              bigint(32) unsigned NOT NULL AUTO_INCREMENT,
  `p_id`            bigint(32) unsigned COMMENT '父级角色',
  `name`            varchar(32)  COMMENT '角色名称',
  `sign`            varchar(32)  COMMENT '角色标识',
  
  `is_active`       tinyint(8) unsigned DEFAULT 1 COMMENT '是否有效/启用',
  `is_deleted`      tinyint(8) unsigned DEFAULT 0 COMMENT '是否逻辑删除',
  `user_create`     bigint(32)   unsigned COMMENT '创建人',
  `user_modified`   bigint(32)   unsigned COMMENT '修改人',
  `ctime`      datetime COMMENT '创建时间',
  `utime`    datetime COMMENT '修改时间',
  `remark`            varchar(255) COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='角色表';

DROP TABLE IF EXISTS `tbl_permission`;

CREATE TABLE `tbl_permission` (
  `id`              bigint(32) unsigned NOT NULL AUTO_INCREMENT,
  `p_id`            bigint(32) unsigned COMMENT '父级权限',
  `type`            varchar(32)  COMMENT '权限类型',
  `name`            varchar(32)  COMMENT '权限名称',
  `sign`            varchar(32)  COMMENT '权限标识',
  
  `is_active`       tinyint(8) unsigned DEFAULT 1 COMMENT '是否有效/启用',
  `is_deleted`      tinyint(8) unsigned DEFAULT 0 COMMENT '是否逻辑删除',
  `user_create`     bigint(32)   unsigned COMMENT '创建人',
  `user_modified`   bigint(32)   unsigned COMMENT '修改人',
  `ctime`      datetime COMMENT '创建时间',
  `utime`    datetime COMMENT '修改时间',
  `remark`            varchar(255) COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='权限表';

DROP TABLE IF EXISTS `tbl_user_group_map`;

CREATE TABLE `tbl_user_group_map` (
  `user_id`         bigint(32) unsigned NOT NULL COMMENT '用户ID',
  `group_id`        bigint(32) unsigned NOT NULL COMMENT '组织ID',
  `group_pid`       bigint(32) unsigned COMMENT '组织父级ID',
  
  `is_active`       tinyint(8) unsigned DEFAULT 1 COMMENT '是否有效/启用',
  `user_create`     bigint(32)   unsigned COMMENT '创建人',
  `user_modified`   bigint(32)   unsigned COMMENT '修改人',
  `ctime`      datetime COMMENT '创建时间',
  `utime`    datetime COMMENT '修改时间',
  `remark`            varchar(255) COMMENT '备注',
  PRIMARY KEY(`user_id`,`group_id`),
  UNIQUE KEY `uk_user_group`(`user_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户组织关联表';

DROP TABLE IF EXISTS `tbl_user_role_map`;

CREATE TABLE `tbl_user_role_map` (
  `user_id`         bigint(32) unsigned NOT NULL COMMENT '用户ID',
  `role_id`         bigint(32) unsigned NOT NULL COMMENT '角色ID',
  `role_pid`        bigint(32) unsigned COMMENT '角色父级ID',
  
  `is_active`       tinyint(8) unsigned DEFAULT 1 COMMENT '是否有效/启用',
  `user_create`     bigint(32)   unsigned COMMENT '创建人',
  `user_modified`   bigint(32)   unsigned COMMENT '修改人',
  `ctime`      datetime COMMENT '创建时间',
  `utime`    datetime COMMENT '修改时间',
  `remark`            varchar(255) COMMENT '备注',
  PRIMARY KEY(`user_id`,`role_id`),
  UNIQUE KEY `uk_user_role`(`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户角色关联表';

DROP TABLE IF EXISTS `tbl_role_permission_map`;

CREATE TABLE `tbl_role_permission_map` (
  `role_id`         bigint(32) unsigned NOT NULL COMMENT '角色ID',
  `permission_id`   bigint(32) unsigned NOT NULL COMMENT '权限ID',
  `permission_pid`  bigint(32) unsigned COMMENT '权限父级ID',
  
  `is_active`       tinyint(8) unsigned DEFAULT 1 COMMENT '是否有效/启用',
  `user_create`     bigint(32)   unsigned COMMENT '创建人',
  `user_modified`   bigint(32)   unsigned COMMENT '修改人',
  `ctime`           datetime COMMENT '创建时间',
  `utime`           datetime COMMENT '修改时间',
  `remark`          varchar(255) COMMENT '备注',
  PRIMARY KEY(`role_id`,`permission_id`),
  UNIQUE KEY `uk_user_role_permission`(`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='角色权限关联表';

