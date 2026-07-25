DROP TABLE IF EXISTS updatenotifiers;

CREATE TABLE IF NOT EXISTS updatenotifier_versions
(
    id      INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    type    TEXT    NOT NULL,
    key     TEXT    NOT NULL,
    version TEXT    NOT NULL,
    raw     TEXT
);

CREATE UNIQUE INDEX idx_updatenotifier_versions_type_key_version ON updatenotifier_versions (type, key, version);
CREATE INDEX idx_updatenotifier_versions_by_type_key ON updatenotifier_versions (type, key);
