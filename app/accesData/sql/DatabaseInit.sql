create table if not exists venues(
    id serial primary key,
    name varchar(255) not null,
    address varchar(255) not null,
    capacity int not null
    );

create table if not exists users(
    id serial primary key,
    name varchar(255) not null,
    email varchar(255) not null,
    balance numeric not null
    );

create table if not exists shows(
    id serial primary key,
    name varchar(255) not null,
    category varchar(255) not null,
    id_venue int not null,

    constraint show_fk_venue
    foreign key(id_venue)
    references venues(id)
    );

create table if not exists performances(
    id serial primary key,
    date Date not null,
    active Bool not null,
    id_show int not null,

    constraint peformance_fk_show
    foreign key (id_show)
    references shows(id)

    );

create table if not exists zones(
    id serial primary key,
    name varchar(255) not null,
    amount int not null,
    price numeric not null,
    id_show int not null,

    constraint zones_fk_show
    foreign key(id_show)
    references shows(id)

    );

create table if not exists bookings(
    id serial primary key,
    amount int not null,
    date Date not null,
    performance_id int not null,
    zone_id int not null,
    user_id int not null,

    constraint bookings_fk_performance
    foreign key(performance_id)
    references performances(id),

    constraint bookings_fk_zone
    foreign key (zone_id)
    references zones(id),

    constraint bookings_fk_user
    foreign key(user_id)
    references users(id)

    );

create table if not exists remaining_tickets(
    id serial primary key,
    amount_available int not null,
    zone_id int not null,
    performance_id int not null,

    constraint remaining_fk_performance
    foreign key(performance_id)
    references performances(id),

    constraint remainig_fk_zone
    foreign key (zone_id)
    references zones(id)
    )