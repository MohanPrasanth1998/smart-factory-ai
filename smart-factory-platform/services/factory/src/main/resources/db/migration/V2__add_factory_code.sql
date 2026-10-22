ALTER TABLE factories ADD COLUMN code VARCHAR(60) NULL;

UPDATE factories SET code = CONCAT('FAC-', LPAD(id, 3, '0')) WHERE code IS null;

ALTER TABLE  factories MODIFY COLUMN  code VARCHAR(50) NOT NULL ;

ALTER TABLE factories ADD CONSTRAINT uk_factories_code UNIQUE (code);