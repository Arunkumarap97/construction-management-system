CREATE EXTENSION IF NOT EXISTS pgcrypto;

---------------------------------------------------
-- CLIENT
---------------------------------------------------
ALTER TABLE client
    ADD COLUMN IF NOT EXISTS guid VARCHAR(36),
    ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

UPDATE client
SET guid = COALESCE(guid, gen_random_uuid()::text),
    active = COALESCE(active, TRUE),
    created_at = COALESCE(created_at, NOW()),
    updated_at = COALESCE(updated_at, NOW());

ALTER TABLE client
    ALTER COLUMN guid SET NOT NULL,
ALTER COLUMN active SET NOT NULL,
ALTER COLUMN created_at SET NOT NULL,
ALTER COLUMN updated_at SET NOT NULL;

DO $$
BEGIN
IF NOT EXISTS (
SELECT 1 FROM pg_constraint WHERE conname='uk_client_guid'
) THEN
ALTER TABLE client
    ADD CONSTRAINT uk_client_guid UNIQUE(guid);
END IF;
END $$;

---------------------------------------------------
-- PROJECT
---------------------------------------------------
ALTER TABLE project
    ADD COLUMN IF NOT EXISTS guid VARCHAR(36),
    ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

UPDATE project
SET guid = COALESCE(guid, gen_random_uuid()::text),
    active = COALESCE(active, TRUE),
    created_at = COALESCE(created_at, NOW()),
    updated_at = COALESCE(updated_at, NOW());

ALTER TABLE project
    ALTER COLUMN guid SET NOT NULL,
ALTER COLUMN active SET NOT NULL,
ALTER COLUMN created_at SET NOT NULL,
ALTER COLUMN updated_at SET NOT NULL;

DO $$
BEGIN
IF NOT EXISTS (
SELECT 1 FROM pg_constraint WHERE conname='uk_project_guid'
) THEN
ALTER TABLE project
    ADD CONSTRAINT uk_project_guid UNIQUE(guid);
END IF;
END $$;

---------------------------------------------------
-- PAYMENT
---------------------------------------------------
ALTER TABLE payment
    ADD COLUMN IF NOT EXISTS guid VARCHAR(36),
    ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

UPDATE payment
SET guid = COALESCE(guid, gen_random_uuid()::text),
    active = COALESCE(active, TRUE),
    created_at = COALESCE(created_at, NOW()),
    updated_at = COALESCE(updated_at, NOW());

ALTER TABLE payment
    ALTER COLUMN guid SET NOT NULL,
ALTER COLUMN active SET NOT NULL,
ALTER COLUMN created_at SET NOT NULL,
ALTER COLUMN updated_at SET NOT NULL;

DO $$
BEGIN
IF NOT EXISTS (
SELECT 1 FROM pg_constraint WHERE conname='uk_payment_guid'
) THEN
ALTER TABLE payment
    ADD CONSTRAINT uk_payment_guid UNIQUE(guid);
END IF;
END $$;

---------------------------------------------------
-- EXPENSE
---------------------------------------------------
ALTER TABLE expense
    ADD COLUMN IF NOT EXISTS guid VARCHAR(36),
    ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

UPDATE expense
SET guid = COALESCE(guid, gen_random_uuid()::text),
    active = COALESCE(active, TRUE),
    created_at = COALESCE(created_at, NOW()),
    updated_at = COALESCE(updated_at, NOW());

ALTER TABLE expense
    ALTER COLUMN guid SET NOT NULL,
ALTER COLUMN active SET NOT NULL,
ALTER COLUMN created_at SET NOT NULL,
ALTER COLUMN updated_at SET NOT NULL;

DO $$
BEGIN
IF NOT EXISTS (
SELECT 1 FROM pg_constraint WHERE conname='uk_expense_guid'
) THEN
ALTER TABLE expense
    ADD CONSTRAINT uk_expense_guid UNIQUE(guid);
END IF;
END $$;

---------------------------------------------------
-- BANK ACCOUNT
---------------------------------------------------
ALTER TABLE bank_account
    ADD COLUMN IF NOT EXISTS guid VARCHAR(36),
    ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

UPDATE bank_account
SET guid = COALESCE(guid, gen_random_uuid()::text),
    active = COALESCE(active, TRUE),
    created_at = COALESCE(created_at, NOW()),
    updated_at = COALESCE(updated_at, NOW());

ALTER TABLE bank_account
    ALTER COLUMN guid SET NOT NULL,
ALTER COLUMN active SET NOT NULL,
ALTER COLUMN created_at SET NOT NULL,
ALTER COLUMN updated_at SET NOT NULL;

DO $$
BEGIN
IF NOT EXISTS (
SELECT 1 FROM pg_constraint WHERE conname='uk_bank_guid'
) THEN
ALTER TABLE bank_account
    ADD CONSTRAINT uk_bank_guid UNIQUE(guid);
END IF;
END $$;

---------------------------------------------------
-- PAYMENT REQUEST
---------------------------------------------------
ALTER TABLE payment_request
    ADD COLUMN IF NOT EXISTS guid VARCHAR(36),
    ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

UPDATE payment_request
SET guid = COALESCE(guid, gen_random_uuid()::text),
    active = COALESCE(active, TRUE),
    created_at = COALESCE(created_at, NOW()),
    updated_at = COALESCE(updated_at, NOW());

ALTER TABLE payment_request
    ALTER COLUMN guid SET NOT NULL,
ALTER COLUMN active SET NOT NULL,
ALTER COLUMN created_at SET NOT NULL,
ALTER COLUMN updated_at SET NOT NULL;

DO $$
BEGIN
IF NOT EXISTS (
SELECT 1 FROM pg_constraint WHERE conname='uk_payment_request_guid'
) THEN
ALTER TABLE payment_request
    ADD CONSTRAINT uk_payment_request_guid UNIQUE(guid);
END IF;
END $$;

---------------------------------------------------
-- APP USER
---------------------------------------------------
ALTER TABLE app_user
    ADD COLUMN IF NOT EXISTS guid VARCHAR(36),
    ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

UPDATE app_user
SET guid = COALESCE(guid, gen_random_uuid()::text),
    active = COALESCE(active, TRUE),
    created_at = COALESCE(created_at, NOW()),
    updated_at = COALESCE(updated_at, NOW());

ALTER TABLE app_user
    ALTER COLUMN guid SET NOT NULL,
ALTER COLUMN active SET NOT NULL,
ALTER COLUMN created_at SET NOT NULL,
ALTER COLUMN updated_at SET NOT NULL;

DO $$
BEGIN
IF NOT EXISTS (
SELECT 1 FROM pg_constraint WHERE conname='uk_app_user_guid'
) THEN
ALTER TABLE app_user
    ADD CONSTRAINT uk_app_user_guid UNIQUE(guid);
END IF;
END $$;