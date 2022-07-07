#配置商品信息item
CREATE TABLE item(
    id int(11) NOT NULL AUTO_INCREMENT,
    code varchar(255) DEFAULT NULL COMMENT '商品编号',
    name varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '商品名称',
    create_time datetime DEFAULT NULL,
    PRIMARY KEY (id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品信息表';

#发红包记录表
CREATE TABLE red_record(
    id int(11) NOT NULL AUTO_INCREMENT,
    user_id int(11) NOT NULL COMMENT '用户id',
    red_packet varchar(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '红包全局唯一标识串',
    total int(11) NOT NULL COMMENT '人数',
    amount decimal(10,2) DEFAULT NULL COMMENT '总金额（单位为分）',
    is_active tinyint(4) DEFAULT '1',
    create_time datetime DEFAULT NULL,
    PRIMARY KEY(id)
)ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='发红包记录';

#红包明细金额表
CREATE TABLE red_detail(
    id int(11) NOT NULL AUTO_INCREMENT,
    record_id int(11) NOT NULL COMMENT '红包记录id',
    amount decimal(8,2) DEFAULT NULL COMMENT '金额（单位为分）',
    is_active tinyint(4) DEFAULT '1',
    create_time datetime DEFAULT Null,
    PRIMARY KEY(id)
)ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8 COMMENT='红包明细金额';

#抢红包记录表
CREATE TABLE red_rob_record(
    id int(11) NOT NULL AUTO_INCREMENT,
    user_id int(11) DEFAULT NULL COMMENT '用户账号',
    red_packet varchar(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '红包全局唯一标识串',
    amount decimal(8,2) DEFAULT NULL COMMENT '金额（单位为分）',
    rob_time datetime DEFAULT NULL COMMENT '时间',
    is_active tinyint(4) DEFAULT '1',
    PRIMARY KEY(id)
)ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 COMMENT='抢红包记录';

#登录用户信息表
CREATE TABLE user(
    id int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
    user_name varchar(255) NOT NULL COMMENT '用户名',
    password varchar(255) NOT NULL COMMENT '密码',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY(id),
    UNIQUE KEY idx_user_name(user_name) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户信息表';

#登录用户日志记录表
CREATE TABLE sys_log(
    id int(11) NOT NULL AUTO_INCREMENT,
    user_id int(11) DEFAULT '0' COMMENT '用户id',
    module varchar(255) DEFAULT NULL COMMENT '用户操作所属模块',
    data varchar(5000) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '操作数据',
    memo varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '备注',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY(id)
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='日志记录表';

#商城用户下单记录表
CREATE TABLE user_order (
    id int(11) NOT NULL AUTO_INCREMENT,
    order_no varchar(255) NOT NULL COMMENT '订单编号',
    user_id int(11) NOT NULL COMMENT '用户id',
    status int(11) DEFAULT NULL COMMENT '状态（1=已保存，2=已付款，3=已取消）',
    is_active int(255) DEFAULT '1' COMMENT '是否失效（1=有效，0=失效）',
    create_time datetime DEFAULT NULL COMMENT '下单时间',
    update_time datetime DEFAULT NULL,
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户下单记录表';

#失效用户下单记录表
CREATE TABLE mq_order(
    id int(11) NOT NULL AUTO_INCREMENT,
    order_id int(11) NOT NULL COMMENT '下单记录id',
    business_time datetime DEFAULT NULL COMMENT '失效下单记录时间',
    memo varchar(255) DEFAULT NULL COMMENT '备注信息',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='RabbitMQ失效下单历史记录表';
