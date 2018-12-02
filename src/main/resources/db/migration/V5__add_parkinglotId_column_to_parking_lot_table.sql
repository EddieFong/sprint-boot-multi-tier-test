alter table if exists parking_lot add column if not exists parking_lot_id varchar(64) not null;
alter table if exists parking_lot add unique(parking_lot_id);