ALTER TABLE MS_ORDER
    ADD COLUMN NUMBER_OF_ORDER UUID DEFAULT gen_random_uuid() NOT NULL;
