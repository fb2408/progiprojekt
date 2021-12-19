
drop table if exists clientreservation;
drop table if exists parkingspotoccupancy;
drop table if exists parkingspot;
drop table if exists parking;
drop table if exists parkingowner;
drop table if exists client;
drop table if exists usertable;


create table usertable
(
    userid        integer      not null
        constraint "User_pkey"
            primary key,
    username      varchar(35)  not null
        constraint "User_username_key"
            unique,
    userfirstname varchar(35)  not null,
    usersurname   varchar(35)  not null,
    useremail     varchar(50)  not null,
    password      varchar(256) not null,
    usertype      varchar(15)  not null,
    confirmed     boolean      not null
);

alter table usertable
    owner to postgres;

create table parkingowner
(
    userid    integer     not null
        constraint parkingowner_pkey
            primary key
        constraint parkingowner_userid_fkey
            references usertable
            on delete cascade,
    iban      char(21)    not null,
    idpicture varchar(50) not null
);

alter table parkingowner
    owner to postgres;

create table parking
(
    userid       integer     not null
        constraint parking_pkey
            primary key
        constraint parking_userid_fkey
            references parkingowner
            on delete cascade,
    parkingname  varchar(50) not null,
    parkingphoto varchar(50),
    hourlyprice  numeric,
    description  varchar(1000)
);

alter table parking
    owner to postgres;

create table parkingspot
(
    userid            integer     not null
        constraint parkingspot_userid_fkey
            references parking
            on delete cascade,
    parkingspotnumber integer     not null,
    parkingspottype   varchar(20) not null
        constraint parkingspot_parkingspottype_check
            check (upper((parkingspottype)::text) = ANY (ARRAY ['BIKE'::text, 'CAR'::text])),
    canbereserved     boolean     not null,
    point1x           numeric,
    point1y           numeric,
    point2x           numeric,
    point2y           numeric,
    point3x           numeric,
    point3y           numeric,
    point4x           numeric,
    point4y           numeric,
    constraint parkingspot_pkey
        primary key (userid, parkingspotnumber)
);

alter table parkingspot
    owner to postgres;

create table parkingspotoccupancy
(
    userid            integer   not null,
    parkingspotnumber integer   not null,
    datefrom          timestamp not null,
    dateto            timestamp,
    occupancy         boolean   not null,
    constraint parkingspotoccupancy_pkey
        primary key (userid, parkingspotnumber, datefrom),
    constraint parkingspotoccupancy_userid_parkingspotnumber_dateto_key
        unique (userid, parkingspotnumber, dateto),
    constraint parkingspotoccupancy_userid_parkingspotnumber_fkey
        foreign key (userid, parkingspotnumber) references parkingspot
            on delete cascade
);

alter table parkingspotoccupancy
    owner to postgres;

create table client
(
    userid        integer not null
        constraint client_pkey
            primary key
        constraint client_userid_fkey
            references usertable,
    walletbalance numeric not null
);

alter table client
    owner to postgres;

create table clientreservation
(
    clientuserid      integer   not null
        constraint clientreservation_clientuserid_fkey
            references client,
    owneruserid       integer   not null,
    parkingspotnumber integer   not null,
    timeofstart       timestamp not null,
    duration          integer   not null
        constraint clientreservation_duration_check
            check ((duration >= 0) AND (duration <= (24 * 7))),
    recurring         boolean   not null,
    constraint clientreservation_pkey
        primary key (clientuserid, timeofstart),
    constraint clientreservation_owneruserid_parkingspotnumber_timeofstart_key
        unique (owneruserid, parkingspotnumber, timeofstart),
    constraint clientreservation_owneruserid_parkingspotnumber_fkey
        foreign key (owneruserid, parkingspotnumber) references parkingspot
);

alter table clientreservation
    owner to postgres;

CREATE OR REPLACE FUNCTION update_owner_client() RETURNS TRIGGER AS
$BODY$
BEGIN
    IF NEW.usertype = 'owner' THEN
        insert into parkingowner(userid, iban, idpicture) values (NEW.userid, 'HR0000000000000000000', '');
    else
        insert into client(userid, walletbalance) values (NEW.userid, 0);
    end if;
    RETURN new;
END;
$BODY$
    language plpgsql;

Create trigger updateOwnerClient
    after insert
    on usertable
    for each row
execute function update_owner_client();


alter table parking
    ADD entrancepointx numeric,
    Add entrancepointy numeric;