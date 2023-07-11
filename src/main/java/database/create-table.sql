create table users(
	id integer primary key auto_increment,
    full_name nvarchar(100),
    user_name varchar(50) unique,
    pass_word varchar(20) not null,
    point integer not null,
    active integer(2) not null
);

create table card(
	id varchar(20) primary key,
    user_id integer,
    note nvarchar(255),
    active integer(2) not null,
    foreign key (user_id) references users(id)
);

create table admin(
	id integer primary key auto_increment,
    full_name nvarchar(100),
    user_name varchar(50) unique,
    pass_word varchar(20) not null,
    active integer(2) not null
);

create table history_exit(
	card_id varchar(20),
    time_io datetime not null,
    image_io longblob,
    type_io int(2) not null,
    note nvarchar(255),
    foreign key(card_id) references card(id)
);

insert into history_exit(card_id,time_io,type_io,image_io,note) values("cardid1","2022-11-19 23:30",0,null,null);
select * from history_exit where card_id="cardid1" and type_io=1 order by time_io;
select * from history_exit where card_id="cardid1" order by time_io desc;



-- web
select * from users where BINARY user_name = ? and binary pass_word=?;
update users set point = 0 where id=1;
select * from users;

