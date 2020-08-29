(ns musicbrainz-importer.mbserver-table-defs)

(def table-defs
  {:alternative_release
    {:id
     :gid
     :release
     :name
     :artist_credit
     :type
     :language
     :script
     :comment}
   :alternative_release_type
    {:id
     :name
     :parent
     :child_order
     :description
     :gid}
 :alternative_medium
    {:id
     :medium
     :alternative_release
     :name}
 :alternative_track
    {:id
     :name
     :artist_credit
     :ref_count}
   
    ; :CHECK (name != '' AND (name IS NOT NULL OR artist_credit IS NOT NULL))
 :alternative_medium_track
    {:alternative_medium
    :track
    :alternative_track} 
 :annotation
    {:id
    :editor
    :text
    :changelog
    :created}
 :application
    {:id
    :owner
    :name
    :oauth_id
    :oauth_secret
    :oauth_redirect_uri}
 :area_type
    {:id
    :name
    :parent
    :child_order
    :description         TEXT,
    :gid}                 uuid NOT NULL
 :area
    {:id                  SERIAL, -- PK
    :gid                 uuid NOT NULL,
    :name                VARCHAR NOT NULL,
    :type                INTEGER, -- references area_type.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >=0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :begin_date_year     SMALLINT,
    :begin_date_month    SMALLINT,
    :begin_date_day      SMALLINT,
    :end_date_year       SMALLINT,
    :end_date_month      SMALLINT,
    :end_date_day        SMALLINT,
    :ended               BOOLEAN NOT NULL DEFAULT FALSE
    :comment}             VARCHAR(255) NOT NULL DEFAULT ''
 :area_gid_redirect
    {:gid                 UUID NOT NULL, -- PK
    :new_id              INTEGER NOT NULL, -- references area.id
    :created}             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :area_alias_type
    {:id                  
    :name                TEXT NOT NULL,
    :parent              INTEGER, -- references area_alias_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid            }     uuid NOT NULL
 :area_alias
    {:id                  SERIAL, --PK
    :area                INTEGER NOT NULL, -- references area.id
    :name                VARCHAR NOT NULL,
    :locale              TEXT,
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >=0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :type                INTEGER, -- references area_alias_type.id
    :sort_name           VARCHAR NOT NULL,
    :begin_date_year     SMALLINT,
    :begin_date_month    SMALLINT,
    :begin_date_day      SMALLINT,
    :end_date_year       SMALLINT,
    :end_date_month      SMALLINT,
    :end_date_day        SMALLINT,
    :primary_for_locale  BOOLEAN NOT NULL DEFAULT false,
    :ended               BOOLEAN NOT NULL DEFAULT FALSE
 :area_annotation
    {:area        INTEGER NOT NULL, -- PK, references area.id
    :annotation  INTEGER NOT NULL -- PK, references annotation.id
 :area_attribute_type
    {:id                  SERIAL,  -- PK
    :name                VARCHAR(255) NOT NULL,
    :comment             VARCHAR(255) NOT NULL DEFAULT '',
    :free_text           BOOLEAN NOT NULL,
    :parent              INTEGER, -- references area_attribute_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :area_attribute_type_allowed_value
    {:id                  SERIAL,  -- PK
    :area_attribute_type INTEGER NOT NULL, -- references area_attribute_type.id
    :value               TEXT,
    :parent              INTEGER, -- references area_attribute_type_allowed_value.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :area_attribute
    {:id                                  SERIAL,  -- PK
    :area                                INTEGER NOT NULL, -- references area.id
    :area_attribute_type                 INTEGER NOT NULL, -- references area_attribute_type.id
    :area_attribute_type_allowed_value   INTEGER, -- references area_attribute_type_allowed_value.id
    :area_attribute_text                 TEXT
 :area_tag
    {:area                INTEGER NOT NULL, -- PK, references area.id
    :tag                 INTEGER NOT NULL, -- PK, references tag.id
    :count               INTEGER NOT NULL,
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :area_tag_raw
    {:area                INTEGER NOT NULL, -- PK, references area.id
    :editor              INTEGER NOT NULL, -- PK, references editor.id
    :tag                 INTEGER NOT NULL, -- PK, references tag.id
    :is_upvote           BOOLEAN NOT NULL DEFAULT TRUE
 :artist
    {:id                  SERIAL,
    :gid                 UUID NOT NULL,
    :name                VARCHAR NOT NULL,
    :sort_name           VARCHAR NOT NULL,
    :begin_date_year     SMALLINT,
    :begin_date_month    SMALLINT,
    :begin_date_day      SMALLINT,
    :end_date_year       SMALLINT,
    :end_date_month      SMALLINT,
    :end_date_day        SMALLINT,
    :type                INTEGER, -- references artist_type.id
    :area                INTEGER, -- references area.id
    :gender              INTEGER, -- references gender.id
    :comment             VARCHAR(255) NOT NULL DEFAULT '',
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :ended               BOOLEAN NOT NULL DEFAULT FALSE
    :begin_area          INTEGER, -- references area.id
    :end_area            INTEGER -- references area.id
 :artist_alias_type
   {:id                  SERIAL,
    :name                TEXT NOT NULL,
    :parent              INTEGER, -- references artist_alias_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :artist_alias
    {:id                  SERIAL,
    :artist              INTEGER NOT NULL, -- references artist.id
    :name                VARCHAR NOT NULL,
    :locale              TEXT,
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :type                INTEGER, -- references artist_alias_type.id
    :sort_name           VARCHAR NOT NULL,
    :begin_date_year     SMALLINT,
    :begin_date_month    SMALLINT,
    :begin_date_day      SMALLINT,
    :end_date_year       SMALLINT,
    :end_date_month      SMALLINT,
    :end_date_day        SMALLINT,
    :primary_for_locale  BOOLEAN NOT NULL DEFAULT false,
    :ended               BOOLEAN NOT NULL DEFAULT FALSE
 :artist_annotation
    {:artist              INTEGER NOT NULL, -- PK, references artist.id
    :annotation          INTEGER NOT NULL -- PK, references annotation.id
 :artist_attribute_type
    {:id                  SERIAL,  -- PK
    :name                VARCHAR(255) NOT NULL,
    :comment             VARCHAR(255) NOT NULL DEFAULT '',
    :free_text           BOOLEAN NOT NULL,
    :parent              INTEGER, -- references artist_attribute_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :artist_attribute_type_allowed_value
    {:id                          SERIAL,  -- PK
    :artist_attribute_type       INTEGER NOT NULL, -- references artist_attribute_type.id
    :value                       TEXT,
    :parent                      INTEGER, -- references artist_attribute_type_allowed_value.id
    :child_order                 INTEGER NOT NULL DEFAULT 0,
    :description                 TEXT,
    :gid                         uuid NOT NULL
 :artist_attribute
    {:id                                  SERIAL,  -- PK
    :artist                              INTEGER NOT NULL, -- references artist.id
    :artist_attribute_type               INTEGER NOT NULL, -- references artist_attribute_type.id
    :artist_attribute_type_allowed_value INTEGER, -- references artist_attribute_type_allowed_value.id
    :artist_attribute_text               TEXT
 :artist_ipi
    :artist              INTEGER NOT NULL, -- PK, references artist.id
    :ipi                 CHAR(11) NOT NULL CHECK (ipi ~ E'^\\d{11}$'), -- PK
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :artist_isni
    {:artist              INTEGER NOT NULL, -- PK, references artist.id
    :isni                CHAR(16) NOT NULL CHECK (isni ~ E'^\\d{15}[\\dX]$'), -- PK
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :artist_meta
    {:id                  INTEGER NOT NULL, -- PK, references artist.id CASCADE
    :rating              SMALLINT CHECK (rating >= 0 AND rating <= 100),
    :rating_count        INTEGER
 :artist_tag
    {:artist              INTEGER NOT NULL, -- PK, references artist.id
    :tag                 INTEGER NOT NULL, -- PK, references tag.id
    :count               INTEGER NOT NULL,
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :artist_rating_raw
    {:artist              INTEGER NOT NULL, -- PK, references artist.id
    :editor              INTEGER NOT NULL, -- PK, references editor.id
    :rating              SMALLINT NOT NULL CHECK (rating >= 0 AND rating <= 100)
 :artist_tag_raw
    {:artist              INTEGER NOT NULL, -- PK, references artist.id
    :editor              INTEGER NOT NULL, -- PK, references editor.id
    :tag                 INTEGER NOT NULL, -- PK, references tag.id
    :is_upvote           BOOLEAN NOT NULL DEFAULT TRUE
 :artist_credit
    {:id                  SERIAL,
    :name                VARCHAR NOT NULL,
    :artist_count        SMALLINT NOT NULL,
    :ref_count           INTEGER DEFAULT 0,
    :created             TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0)
 :artist_credit_name
    {:artist_credit       INTEGER NOT NULL, -- PK, references artist_credit.id CASCADE
    :position            SMALLINT NOT NULL, -- PK
    :artist              INTEGER NOT NULL, -- references artist.id CASCADE
    :name                VARCHAR NOT NULL,
    :join_phrase         TEXT NOT NULL DEFAULT ''
 :artist_gid_redirect
    {:gid                 UUID NOT NULL, -- PK
    :new_id              INTEGER NOT NULL, -- references artist.id
    :created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :artist_type
    {:id                  SERIAL,
    :name                VARCHAR(255) NOT NULL,
    :parent              INTEGER, -- references artist_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :autoeditor_election
    {:id                  SERIAL,
    :candidate           INTEGER NOT NULL, -- references editor.id
    :proposer            INTEGER NOT NULL, -- references editor.id
    :seconder_1          INTEGER, -- references editor.id
    :seconder_2          INTEGER, -- references editor.id
    :status              INTEGER NOT NULL DEFAULT 1
    :                        CHECK (status IN (1,2,3,4,5,6)),
    :                        -- 1 : has proposer
    :                        -- 2 : has seconder_1
    :                        -- 3 : has seconder_2 (voting open)
    :                        -- 4 : accepted!
    :                        -- 5 : rejected
    :                        -- 6 : cancelled (by proposer)
    :yes_votes           INTEGER NOT NULL DEFAULT 0,
    :no_votes            INTEGER NOT NULL DEFAULT 0,
    :propose_time        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    :open_time           TIMESTAMP WITH TIME ZONE,
    :close_time          TIMESTAMP WITH TIME ZONE
 :autoeditor_election_vote
    {:id                  SERIAL,
    :autoeditor_election INTEGER NOT NULL, -- references autoeditor_election.id
    :voter               INTEGER NOT NULL, -- references editor.id
    :vote                INTEGER NOT NULL CHECK (vote IN (-1,0,1)),
    :vote_time           TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
 :cdtoc
    {:id                  SERIAL,
    :discid              CHAR(28) NOT NULL,
    :freedb_id           CHAR(8) NOT NULL,
    :track_count         INTEGER NOT NULL,
    :leadout_offset      INTEGER NOT NULL,
    :track_offset        INTEGER[] NOT NULL,
    :degraded            BOOLEAN NOT NULL DEFAULT FALSE,
    :created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :cdtoc_raw
    {:id                  SERIAL, -- PK
    :release             INTEGER NOT NULL, -- references release_raw.id
    :discid              CHAR(28) NOT NULL,
    :track_count          INTEGER NOT NULL,
    :leadout_offset       INTEGER NOT NULL,
    :track_offset         INTEGER[] NOT NULL
 :country_area
    {:area                INTEGER -- PK, references area.id
 :deleted_entity
    {:gid                 UUID NOT NULL, -- PK
    :data                 JSONB NOT NULL,
    :deleted_at           timestamptz NOT NULL DEFAULT now()
 :edit
    {:id                  SERIAL,
    :editor              INTEGER NOT NULL, -- references editor.id
    :type                SMALLINT NOT NULL,
    :status              SMALLINT NOT NULL,
    :autoedit            SMALLINT NOT NULL DEFAULT 0,
    :open_time            TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :close_time           TIMESTAMP WITH TIME ZONE,
    :expire_time          TIMESTAMP WITH TIME ZONE NOT NULL,
    :language            INTEGER, -- references language.id
    :quality             SMALLINT NOT NULL DEFAULT 1
 :edit_data
    {:edit                INTEGER NOT NULL, -- PK, references edit.id
    :data                JSONB NOT NULL
 :edit_note
    {:id                  SERIAL,
    :editor              INTEGER NOT NULL, -- references editor.id
    :edit                INTEGER NOT NULL, -- references edit.id
    :text                TEXT NOT NULL,
    :post_time            TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :edit_note_recipient
    {:recipient           INTEGER NOT NULL, -- PK, references editor.id
    :edit_note           INTEGER NOT NULL  -- PK, references edit_note.id
 :edit_area
    {:edit                INTEGER NOT NULL, -- PK, references edit.id
    :area                INTEGER NOT NULL  -- PK, references area.id CASCADE
 :edit_artist
    {:edit                INTEGER NOT NULL, -- PK, references edit.id
    :artist              INTEGER NOT NULL, -- PK, references artist.id CASCADE
    :status              SMALLINT NOT NULL -- materialized from edit.status
 :edit_event
    {:edit                INTEGER NOT NULL, -- PK, references edit.id
    :event               INTEGER NOT NULL  -- PK, references event.id CASCADE
 :edit_instrument
    {:edit                INTEGER NOT NULL, -- PK, references edit.id
    :instrument          INTEGER NOT NULL  -- PK, references instrument.id CASCADE
 :edit_label
    {:edit                INTEGER NOT NULL, -- PK, references edit.id
    :label               INTEGER NOT NULL, -- PK, references label.id CASCADE
    :status              SMALLINT NOT NULL -- materialized from edit.status
 :edit_place
    {:edit                INTEGER NOT NULL, -- PK, references edit.id
    :place               INTEGER NOT NULL  -- PK, references place.id CASCADE
 :edit_release
    {:edit                INTEGER NOT NULL, -- PK, references edit.id
    :release             INTEGER NOT NULL  -- PK, references release.id CASCADE
 :edit_release_group
    {:edit                INTEGER NOT NULL, -- PK, references edit.id
    :release_group       INTEGER NOT NULL  -- PK, references release_group.id CASCADE
 :edit_recording
    {:edit                INTEGER NOT NULL, -- PK, references edit.id
    :recording           INTEGER NOT NULL  -- PK, references recording.id CASCADE
 :edit_series
    {:edit                INTEGER NOT NULL, -- PK, references edit.id
    :series              INTEGER NOT NULL  -- PK, references series.id CASCADE
 :edit_work
    {:edit                INTEGER NOT NULL, -- PK, references edit.id
    :work                INTEGER NOT NULL  -- PK, references work.id CASCADE
 :edit_url
    {:edit                INTEGER NOT NULL, -- PK, references edit.id
    :url}                 INTEGER NOT NULL  -- PK, references url.id CASCADE
 :editor
    {:id                  SERIAL,
    :name                VARCHAR(64) NOT NULL,
    :privs               INTEGER DEFAULT 0,
    :email               VARCHAR(64) DEFAULT NULL,
    :website             VARCHAR(255) DEFAULT NULL,
    :bio                 TEXT DEFAULT NULL,
    :member_since        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :email_confirm_date  TIMESTAMP WITH TIME ZONE,
    :last_login_date     TIMESTAMP WITH TIME ZONE DEFAULT now(),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :birth_date          DATE,
    :gender              INTEGER, -- references gender.id
    :area                INTEGER, -- references area.id
    :password            VARCHAR(128) NOT NULL,
    :ha1                 CHAR(32) NOT NULL,
    :deleted             BOOLEAN NOT NULL DEFAULT FALSE
 :old_editor_name
    :name    VARCHAR(64) NOT NULL

; CREATE TYPE FLUENCY AS ENUM ('basic', 'intermediate', 'advanced', 'native');
 :editor_language
     :editor   INTEGER NOT NULL,  -- PK, references editor.id
    :language INTEGER NOT NULL,  -- PK, references language.id
    :fluency  FLUENCY NOT NULL
 :editor_preference
    :id                  SERIAL,
    :editor              INTEGER NOT NULL, -- references editor.id
    :name                VARCHAR(50) NOT NULL,
    :value               VARCHAR(100) NOT NULL
 :editor_subscribe_artist
    :id                  SERIAL,
    :editor              INTEGER NOT NULL, -- references editor.id
    :artist              INTEGER NOT NULL, -- references artist.id
    :last_edit_sent      INTEGER NOT NULL -- references edit.id
 :editor_subscribe_artist_deleted
    :editor INTEGER NOT NULL, -- PK, references editor.id
    :gid UUID NOT NULL, -- PK, references deleted_entity.gid
    :deleted_by INTEGER NOT NULL -- references edit.id
 :editor_subscribe_collection
    :id                  SERIAL,
    :editor              INTEGER NOT NULL,              -- references editor.id
    :collection          INTEGER NOT NULL,              -- weakly references editor_collection.id
    :last_edit_sent      INTEGER NOT NULL,              -- weakly references edit.id
    :available           BOOLEAN NOT NULL DEFAULT TRUE,
    :last_seen_name      VARCHAR(255)
 :editor_subscribe_label
    :id                  SERIAL,
    :editor              INTEGER NOT NULL, -- references editor.id
    :label               INTEGER NOT NULL, -- references label.id
    :last_edit_sent      INTEGER NOT NULL -- references edit.id
 :editor_subscribe_label_deleted
    :editor INTEGER NOT NULL, -- PK, references editor.id
    :gid UUID NOT NULL, -- PK, references deleted_entity.gid
    :deleted_by INTEGER NOT NULL -- references edit.id
 :editor_subscribe_editor
    :id                  SERIAL,
    :editor              INTEGER NOT NULL, -- references editor.id (the one who has subscribed)
    :subscribed_editor   INTEGER NOT NULL, -- references editor.id (the one being subscribed)
    :last_edit_sent      INTEGER NOT NULL  -- weakly references edit.id
 :editor_subscribe_series
    :id                  SERIAL,
    :editor              INTEGER NOT NULL, -- references editor.id
    :series              INTEGER NOT NULL, -- references series.id
    :last_edit_sent      INTEGER NOT NULL -- references edit.id
 :editor_subscribe_series_deleted
    :editor              INTEGER NOT NULL, -- PK, references editor.id
    :gid                 UUID NOT NULL, -- PK, references deleted_entity.gid
    :deleted_by          INTEGER NOT NULL -- references edit.id
 :event
    :id                  SERIAL,
    :gid                 UUID NOT NULL,
    :name                VARCHAR NOT NULL,
    :begin_date_year     SMALLINT,
    :begin_date_month    SMALLINT,
    :begin_date_day      SMALLINT,
    :end_date_year       SMALLINT,
    :end_date_month      SMALLINT,
    :end_date_day        SMALLINT,
    :time                TIME WITHOUT TIME ZONE,
    :type                INTEGER, -- references event_type.id
    :cancelled           BOOLEAN NOT NULL DEFAULT FALSE,
    :setlist             TEXT,
    :comment             VARCHAR(255) NOT NULL DEFAULT '',
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :ended               BOOLEAN NOT NULL DEFAULT FALSE
;       CONSTRAINT event_ended_check CHECK (
;         (
;           -- If any end date fields are not null, then ended must be true
;           (end_date_year IS NOT NULL OR
;            end_date_month IS NOT NULL OR
;            end_date_day IS NOT NULL) AND
;           ended = TRUE
;         ) OR (
;           -- Otherwise, all end date fields must be null
;           (end_date_year IS NULL AND
;            end_date_month IS NULL AND
;            end_date_day IS NULL)
;         )
;       )
; );

; CREATE TYPE event_art_presence AS ENUM ('absent', 'present', 'darkened');

 :event_meta
    :id                  INTEGER NOT NULL, -- PK, references event.id CASCADE
    :rating              SMALLINT CHECK (rating >= 0 AND rating <= 100),
    :rating_count        INTEGER,
    :event_art_presence  event_art_presence NOT NULL DEFAULT 'absent'
 :event_rating_raw
    :event               INTEGER NOT NULL, -- PK, references event.id
    :editor              INTEGER NOT NULL, -- PK, references editor.id
    :rating              SMALLINT NOT NULL CHECK (rating >= 0 AND rating <= 100)
 :event_tag_raw
    :event               INTEGER NOT NULL, -- PK, references event.id
    :editor              INTEGER NOT NULL, -- PK, references editor.id
    :tag                 INTEGER NOT NULL, -- PK, references tag.id
    :is_upvote           BOOLEAN NOT NULL DEFAULT TRUE
 :event_alias_type
    :id                  SERIAL,
    :name                TEXT NOT NULL,
    :parent              INTEGER, -- references event_alias_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :event_alias
    : id                  SERIAL,
    :event               INTEGER NOT NULL, -- references event.id
    :name                VARCHAR NOT NULL,
    :locale              TEXT,
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :type                INTEGER, -- references event_alias_type.id
    :sort_name           VARCHAR NOT NULL,
    :begin_date_year     SMALLINT,
    :begin_date_month    SMALLINT,
    :begin_date_day      SMALLINT,
    :end_date_year       SMALLINT,
    :end_date_month      SMALLINT,
    :end_date_day        SMALLINT,
    :primary_for_locale  BOOLEAN NOT NULL DEFAULT false,
    :ended               BOOLEAN NOT NULL DEFAULT FALSE
 :event_annotation
    :event               INTEGER NOT NULL, -- PK, references event.id
    :annotation          INTEGER NOT NULL -- PK, references annotation.id
 :event_attribute_type
    :id                  SERIAL,  -- PK
    :name                VARCHAR(255) NOT NULL,
    :comment             VARCHAR(255) NOT NULL DEFAULT '',
    :free_text           BOOLEAN NOT NULL,
    :parent              INTEGER, -- references event_attribute_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :event_attribute_type_allowed_value
    :id                          SERIAL,  -- PK
    :event_attribute_type        INTEGER NOT NULL, -- references event_attribute_type.id
    :value                       TEXT,
    :parent                      INTEGER, -- references event_attribute_type_allowed_value.id
    :child_order                 INTEGER NOT NULL DEFAULT 0,
    :description                 TEXT,
    :gid                         uuid NOT NULL
 :event_attribute
    :id                                  SERIAL,  -- PK
    :event                               INTEGER NOT NULL, -- references event.id
    :event_attribute_type                INTEGER NOT NULL, -- references event_attribute_type.id
    :event_attribute_type_allowed_value  INTEGER, -- references event_attribute_type_allowed_value.id
    :event_attribute_text                TEXT
 :event_gid_redirect
    :gid                 UUID NOT NULL, -- PK
    :new_id              INTEGER NOT NULL, -- references event.id
    :created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :event_tag
    :event               INTEGER NOT NULL, -- PK, references event.id
    :tag                 INTEGER NOT NULL, -- PK, references tag.id
    :count               INTEGER NOT NULL,
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :event_type
    :id                  SERIAL,
    :name                VARCHAR(255) NOT NULL,
    :parent              INTEGER, -- references event_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :gender
    :id                  SERIAL,
    :name                VARCHAR(255) NOT NULL,
    :parent              INTEGER, -- references gender.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :genre
    :id                  SERIAL, -- PK
    :gid                 UUID NOT NULL,
    :name                VARCHAR NOT NULL,
    :comment             VARCHAR(255) NOT NULL DEFAULT '',
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >=0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :genre_alias
    : id                  SERIAL,
    :genre               INTEGER NOT NULL, -- references genre.id
    :name                VARCHAR NOT NULL,
    :locale              TEXT,
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :primary_for_locale  BOOLEAN NOT NULL DEFAULT FALSE,
    :CONSTRAINT primary_check CHECK ((locale IS NULL AND primary_for_locale IS FALSE) OR (locale IS NOT NULL))
 :instrument_type
    :id                  SERIAL, -- PK
    :name                VARCHAR(255) NOT NULL,
    :parent              INTEGER, -- references instrument_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :instrument
    :id                  SERIAL, -- PK
    :gid                 uuid NOT NULL,
    :name                VARCHAR NOT NULL,
    :type                INTEGER, -- references instrument_type.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >=0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :comment             VARCHAR(255) NOT NULL DEFAULT '',
    :description         TEXT NOT NULL DEFAULT ''
 :instrument_gid_redirect
    :gid                 UUID NOT NULL, -- PK
    :new_id              INTEGER NOT NULL, -- references instrument.id
    :created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :instrument_alias_type
    :id                  SERIAL, -- PK,
    :name                TEXT NOT NULL,
    :parent              INTEGER, -- references instrument_alias_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :instrument_alias
    :id                  SERIAL, --PK
    :instrument          INTEGER NOT NULL, -- references instrument.id
    :name                VARCHAR NOT NULL,
    :locale              TEXT,
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >=0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :type                INTEGER, -- references instrument_alias_type.id
    :sort_name           VARCHAR NOT NULL,
    :begin_date_year     SMALLINT,
    :begin_date_month    SMALLINT,
    :begin_date_day      SMALLINT,
    :end_date_year       SMALLINT,
    :end_date_month      SMALLINT,
    :end_date_day        SMALLINT,
    :primary_for_locale  BOOLEAN NOT NULL DEFAULT false,
    :ended               BOOLEAN NOT NULL DEFAULT FALSE
 :instrument_annotation
    :instrument  INTEGER NOT NULL, -- PK, references instrument.id
    :annotation  INTEGER NOT NULL -- PK, references annotation.id
 :instrument_attribute_type
    :id                  SERIAL,  -- PK
    :name                VARCHAR(255) NOT NULL,
    :comment             VARCHAR(255) NOT NULL DEFAULT '',
    :free_text           BOOLEAN NOT NULL,
    :parent              INTEGER, -- references instrument_attribute_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :instrument_attribute_type_allowed_value
    :id                          SERIAL,  -- PK
    :instrument_attribute_type   INTEGER NOT NULL, -- references instrument_attribute_type.id
    :value                       TEXT,
    :parent                      INTEGER, -- references instrument_attribute_type_allowed_value.id
    :child_order                 INTEGER NOT NULL DEFAULT 0,
    :description                 TEXT,
    :gid                         uuid NOT NULL
 :instrument_attribute
    :id                                          SERIAL,  -- PK
    :instrument                                  INTEGER NOT NULL, -- references instrument.id
    :instrument_attribute_type                   INTEGER NOT NULL, -- references instrument_attribute_type.id
    :instrument_attribute_type_allowed_value     INTEGER, -- references instrument_attribute_type_allowed_value.id
    :instrument_attribute_text                   TEXT
 :instrument_tag
    :instrument          INTEGER NOT NULL, -- PK, references instrument.id
    :tag                 INTEGER NOT NULL, -- PK, references tag.id
    :count               INTEGER NOT NULL,
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :instrument_tag_raw
    :instrument          INTEGER NOT NULL, -- PK, references instrument.id
    :editor              INTEGER NOT NULL, -- PK, references editor.id
    :tag                 INTEGER NOT NULL, -- PK, references tag.id
    :is_upvote           BOOLEAN NOT NULL DEFAULT TRUE
 :iso_3166_1
    : area      INTEGER NOT NULL, -- references area.id
    :code      CHAR(2) -- PK
 :iso_3166_2
    : area      INTEGER NOT NULL, -- references area.id
    :code      VARCHAR(10) -- PK
 :iso_3166_3
    : area      INTEGER NOT NULL, -- references area.id
    :code      CHAR(4) -- PK
 :isrc
    : id                  SERIAL,
    :recording           INTEGER NOT NULL, -- references recording.id
    :isrc                CHAR(12) NOT NULL CHECK (isrc ~ E'^[A-Z]{2}[A-Z0-9]{3}[0-9]{7}$'),
    :source              SMALLINT,
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :iswc
    : id SERIAL NOT NULL,
    :work INTEGER NOT NULL, -- references work.id
    :iswc CHARACTER(15) CHECK (iswc ~ E'^T-?\\d{3}.?\\d{3}.?\\d{3}[-.]?\\d$'),
    :source SMALLINT,
    :edits_pending INTEGER NOT NULL DEFAULT 0,
    :created TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
 :l_area_area
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references area.id
    :entity1             INTEGER NOT NULL, -- references area.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_area_artist
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references area.id
    :entity1             INTEGER NOT NULL, -- references artist.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_area_event
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references area.id
    :entity1             INTEGER NOT NULL, -- references event.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_area_instrument
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references area.id
    :entity1             INTEGER NOT NULL, -- references instrument.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_area_label
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references area.id
    :entity1             INTEGER NOT NULL, -- references label.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_area_place
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references area.id
    :entity1             INTEGER NOT NULL, -- references place.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_area_recording
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references area.id
    :entity1             INTEGER NOT NULL, -- references recording.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_area_release
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references area.id
    :entity1             INTEGER NOT NULL, -- references release.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_area_release_group
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references area.id
    :entity1             INTEGER NOT NULL, -- references release_group.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_area_series
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references area.id
    :entity1             INTEGER NOT NULL, -- references series.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_area_url
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references area.id
    :entity1             INTEGER NOT NULL, -- references url.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_area_work
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references area.id
    :entity1             INTEGER NOT NULL, -- references work.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_artist_artist
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references artist.id
    :entity1             INTEGER NOT NULL, -- references artist.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_artist_event
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references artist.id
    :entity1             INTEGER NOT NULL, -- references event.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_artist_instrument
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references artist.id
    :entity1             INTEGER NOT NULL, -- references instrument.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_artist_label
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references artist.id
    :entity1             INTEGER NOT NULL, -- references label.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_artist_place
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references artist.id
    :entity1             INTEGER NOT NULL, -- references place.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_artist_recording
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references artist.id
    :entity1             INTEGER NOT NULL, -- references recording.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_artist_release
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references artist.id
    :entity1             INTEGER NOT NULL, -- references release.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_artist_release_group
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references artist.id
    :entity1             INTEGER NOT NULL, -- references release_group.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_artist_series
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references artist.id
    :entity1             INTEGER NOT NULL, -- references series.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_artist_url
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references artist.id
    :entity1             INTEGER NOT NULL, -- references url.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_artist_work
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references artist.id
    :entity1             INTEGER NOT NULL, -- references work.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_event_event
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references event.id
    :entity1             INTEGER NOT NULL, -- references event.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_event_instrument
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references event.id
    :entity1             INTEGER NOT NULL, -- references instrument.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_event_label
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references event.id
    :entity1             INTEGER NOT NULL, -- references label.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_event_place
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references event.id
    :entity1             INTEGER NOT NULL, -- references place.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_event_recording
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references event.id
    :entity1             INTEGER NOT NULL, -- references recording.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_event_release
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references event.id
    :entity1             INTEGER NOT NULL, -- references release.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_event_release_group
    : id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references event.id
    :entity1             INTEGER NOT NULL, -- references release_group.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_event_series
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references event.id
    :entity1             INTEGER NOT NULL, -- references series.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_event_url
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references event.id
    :entity1             INTEGER NOT NULL, -- references url.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_event_work
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references event.id
    entity1             INTEGER NOT NULL, -- references work.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_label_label
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references label.id
    entity1             INTEGER NOT NULL, -- references label.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_instrument_instrument
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references instrument.id
    :entity1             INTEGER NOT NULL, -- references instrument.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_instrument_label
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references instrument.id
    entity1             INTEGER NOT NULL, -- references label.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_instrument_place
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references instrument.id
    entity1             INTEGER NOT NULL, -- references place.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_instrument_recording
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references instrument.id
    entity1             INTEGER NOT NULL, -- references recording.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_instrument_release
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references instrument.id
    entity1             INTEGER NOT NULL, -- references release.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_instrument_release_group
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references instrument.id
    entity1             INTEGER NOT NULL, -- references release_group.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_instrument_series
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references instrument.id
    entity1             INTEGER NOT NULL, -- references series.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_instrument_url
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references instrument.id
    entity1             INTEGER NOT NULL, -- references url.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_instrument_work
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references instrument.id
    :entity1             INTEGER NOT NULL, -- references work.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_label_place
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references label.id
    :entity1             INTEGER NOT NULL, -- references place.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_label_recording
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references label.id
    :entity1             INTEGER NOT NULL, -- references recording.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_label_release
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references label.id
    :entity1             INTEGER NOT NULL, -- references release.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_label_release_group
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references label.id
    entity1             INTEGER NOT NULL, -- references release_group.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_label_series
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references label.id
    entity1             INTEGER NOT NULL, -- references series.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_label_url
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references label.id
    entity1             INTEGER NOT NULL, -- references url.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_label_work
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references label.id
    entity1             INTEGER NOT NULL, -- references work.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_place_place
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references place.id
    entity1             INTEGER NOT NULL, -- references place.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_place_recording
    id                  SERIAL,
    link                INTEGER NOT NULL, -- references link.id
    entity0             INTEGER NOT NULL, -- references place.id
    entity1             INTEGER NOT NULL, -- references recording.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    entity0_credit      TEXT NOT NULL DEFAULT '',
    entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_place_release
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references place.id
    :entity1             INTEGER NOT NULL, -- references release.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_place_release_group
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references place.id
    :entity1             INTEGER NOT NULL, -- references release_group.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_place_series
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references place.id
    :entity1             INTEGER NOT NULL, -- references series.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_place_url
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references place.id
    :entity1             INTEGER NOT NULL, -- references url.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_place_work
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references place.id
    :entity1             INTEGER NOT NULL, -- references work.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_recording_recording
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references recording.id
    :entity1             INTEGER NOT NULL, -- references recording.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_recording_release
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references recording.id
    :entity1             INTEGER NOT NULL, -- references release.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_recording_release_group
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references recording.id
    :entity1             INTEGER NOT NULL, -- references release_group.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_recording_series
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references recording.id
    :entity1             INTEGER NOT NULL, -- references series.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_recording_url
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references recording.id
    :entity1             INTEGER NOT NULL, -- references url.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_recording_work
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references recording.id
    :entity1             INTEGER NOT NULL, -- references work.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_release_release
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references release.id
    :entity1             INTEGER NOT NULL, -- references release.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_release_release_group
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references release.id
    :entity1             INTEGER NOT NULL, -- references release_group.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_release_series
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references release.id
    :entity1             INTEGER NOT NULL, -- references series.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_release_url
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references release.id
    :entity1             INTEGER NOT NULL, -- references url.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_release_work
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references release.id
    :entity1             INTEGER NOT NULL, -- references work.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_release_group_release_group
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references release_group.id
    :entity1             INTEGER NOT NULL, -- references release_group.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_release_group_series
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references release_group.id
    :entity1             INTEGER NOT NULL, -- references series.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_release_group_url
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references release_group.id
    :entity1             INTEGER NOT NULL, -- references url.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_release_group_work
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references release_group.id
    :entity1             INTEGER NOT NULL, -- references work.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_series_series
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references series.id
    :entity1             INTEGER NOT NULL, -- references series.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_series_url
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references series.id
    :entity1             INTEGER NOT NULL, -- references url.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_series_work
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references series.id
    :entity1             INTEGER NOT NULL, -- references work.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_url_url
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references url.id
    :entity1             INTEGER NOT NULL, -- references url.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_url_work
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references url.id
    :entity1             INTEGER NOT NULL, -- references work.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :l_work_work
    :id                  SERIAL,
    :link                INTEGER NOT NULL, -- references link.id
    :entity0             INTEGER NOT NULL, -- references work.id
    :entity1             INTEGER NOT NULL, -- references work.id
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :link_order          INTEGER NOT NULL DEFAULT 0 CHECK (link_order >= 0),
    :entity0_credit      TEXT NOT NULL DEFAULT '',
    :entity1_credit      TEXT NOT NULL DEFAULT ''
 :label
;     id                  SERIAL,
;     gid                 UUID NOT NULL,
;     name                VARCHAR NOT NULL,
;     begin_date_year     SMALLINT,
;     begin_date_month    SMALLINT,
;     begin_date_day      SMALLINT,
;     end_date_year       SMALLINT,
;     end_date_month      SMALLINT,
;     end_date_day        SMALLINT,
;     label_code          INTEGER CHECK (label_code > 0 AND label_code < 100000),
;     type                INTEGER, -- references label_type.id
;     area                INTEGER, -- references area.id
;     comment             VARCHAR(255) NOT NULL DEFAULT '',
;     edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
;     last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     ended               BOOLEAN NOT NULL DEFAULT FALSE
;
 :label_rating_raw
    :label               INTEGER NOT NULL, -- PK, references label.id
    :editor              INTEGER NOT NULL, -- PK, references editor.id
    :rating              SMALLINT NOT NULL CHECK (rating >= 0 AND rating <= 100)
 :label_tag_raw
    :label               INTEGER NOT NULL, -- PK, references label.id
    :editor              INTEGER NOT NULL, -- PK, references editor.id
    :tag                 INTEGER NOT NULL, -- PK, references tag.id
    :is_upvote           BOOLEAN NOT NULL DEFAULT TRUE
 :label_alias_type
    :id                  SERIAL,
    :name                TEXT NOT NULL,
    :parent              INTEGER, -- references label_alias_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :label_alias
    :id                  SERIAL,
    :label               INTEGER NOT NULL, -- references label.id
    :name                VARCHAR NOT NULL,
    :locale              TEXT,
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :type                INTEGER, -- references label_alias_type.id
    :sort_name           VARCHAR NOT NULL,
    :begin_date_year     SMALLINT,
    :begin_date_month    SMALLINT,
    :begin_date_day      SMALLINT,
    :end_date_year       SMALLINT,
    :end_date_month      SMALLINT,
    :end_date_day        SMALLINT,
    :primary_for_locale  BOOLEAN NOT NULL DEFAULT false,
    :ended               BOOLEAN NOT NULL DEFAULT FALSE
 :label_annotation
    :label               INTEGER NOT NULL, -- PK, references label.id
    ; annotation          INTEGER NOT NULL -- PK, references annotation.id
 :label_attribute_type
    ; id                  SERIAL,  -- PK
    ; name                VARCHAR(255) NOT NULL,
    ; comment             VARCHAR(255) NOT NULL DEFAULT '',
    ; free_text           BOOLEAN NOT NULL,
    ; parent              INTEGER, -- references label_attribute_type.id
    ; child_order         INTEGER NOT NULL DEFAULT 0,
    ; description         TEXT,
    ; gid                 uuid NOT NULL
 :label_attribute_type_allowed_value
    ; id                          SERIAL,  -- PK
    ; label_attribute_type        INTEGER NOT NULL, -- references label_attribute_type.id
    ; value                       TEXT,
    ; parent                      INTEGER, -- references label_attribute_type_allowed_value.id
    ; child_order                 INTEGER NOT NULL DEFAULT 0,
    ; description                 TEXT,
    ; gid                         uuid NOT NULL
 :label_attribute
    ; id                                  SERIAL,  -- PK
    ; label                               INTEGER NOT NULL, -- references label.id
    ; label_attribute_type                INTEGER NOT NULL, -- references label_attribute_type.id
    ; label_attribute_type_allowed_value  INTEGER, -- references label_attribute_type_allowed_value.id
    ; label_attribute_text                TEXT
 :label_ipi
    ; label               INTEGER NOT NULL, -- PK, references label.id
    ; ipi                 CHAR(11) NOT NULL CHECK (ipi ~ E'^\\d{11}$'), -- PK
    ; edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    ; created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :label_isni
    ; label               INTEGER NOT NULL, -- PK, references label.id
    ; isni                CHAR(16) NOT NULL CHECK (isni ~ E'^\\d{15}[\\dX]$'), -- PK
    ; edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    ; created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :label_meta
    ; id                  INTEGER NOT NULL, -- PK, references label.id CASCADE
    ; rating              SMALLINT CHECK (rating >= 0 AND rating <= 100),
    ; rating_count        INTEGER
 :label_gid_redirect
    ; gid                 UUID NOT NULL, -- PK
    ; new_id              INTEGER NOT NULL, -- references label.id
    ; created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :label_tag
    ; label               INTEGER NOT NULL, -- PK, references label.id
    ; tag                 INTEGER NOT NULL, -- PK, references tag.id
    ; count               INTEGER NOT NULL,
    ; last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :label_type
    ; id                  SERIAL,
    ; name                VARCHAR(255) NOT NULL,
    ; parent              INTEGER, -- references label_type.id
    ; child_order         INTEGER NOT NULL DEFAULT 0,
    ; description         TEXT,
    ; gid                 uuid NOT NULL
 :language
    ; id                  SERIAL,
    ; iso_code_2t         CHAR(3), -- ISO 639-2 (T)
    ; iso_code_2b         CHAR(3), -- ISO 639-2 (B)
    ; iso_code_1          CHAR(2), -- ISO 639
    ; name                VARCHAR(100) NOT NULL,
    ; frequency           INTEGER NOT NULL DEFAULT 0,
    ; iso_code_3          CHAR(3)  -- ISO 639-3
 :link
    ; id                  SERIAL,
    ; link_type           INTEGER NOT NULL, -- references link_type.id
    ; begin_date_year     SMALLINT,
    ; begin_date_month    SMALLINT,
    ; begin_date_day      SMALLINT,
    ; end_date_year       SMALLINT,
    ; end_date_month      SMALLINT,
    ; end_date_day        SMALLINT,
    ; attribute_count     INTEGER NOT NULL DEFAULT 0,
    ; created             TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    ; ended               BOOLEAN NOT NULL DEFAULT FALSE
 :link_attribute
    ;  link                INTEGER NOT NULL, -- PK, references link.id
    ; attribute_type      INTEGER NOT NULL, -- PK, references link_attribute_type.id
    ; created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :link_attribute_type
    ;  id                  SERIAL,
    ; parent              INTEGER, -- references link_attribute_type.id
    ; root                INTEGER NOT NULL, -- references link_attribute_type.id
    ; child_order         INTEGER NOT NULL DEFAULT 0,
    ; gid                 UUID NOT NULL,
    ; name                VARCHAR(255) NOT NULL,
    ; description         TEXT,
    ; last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :link_creditable_attribute_type
   ; attribute_type INT NOT NULL -- PK, references link_attribute_type.id CASCADE
 :link_attribute_credit
  ;  link INT NOT NULL, -- PK, references link.id
  ; attribute_type INT NOT NULL, -- PK, references link_creditable_attribute_type.attribute_type
  ; credited_as TEXT NOT NULL
 :link_text_attribute_type
     ; attribute_type      INT NOT NULL -- PK, references link_attribute_type.id CASCADE
 :link_attribute_text_value
    : link                INT NOT NULL, -- PK, references link.id
    :attribute_type      INT NOT NULL, -- PK, references link_text_attribute_type.attribute_type
    :text_value          TEXT NOT NULL
 :link_type
    : id                  SERIAL,
    :parent              INTEGER, -- references link_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :gid                 UUID NOT NULL,
    :entity_type0        VARCHAR(50) NOT NULL,
    :entity_type1        VARCHAR(50) NOT NULL,
    :name                VARCHAR(255) NOT NULL,
    :description         TEXT,
    :link_phrase         VARCHAR(255) NOT NULL,
    :reverse_link_phrase VARCHAR(255) NOT NULL,
    :long_link_phrase    VARCHAR(255) NOT NULL,
    :priority            INTEGER NOT NULL DEFAULT 0,
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :is_deprecated       BOOLEAN NOT NULL DEFAULT false,
    :has_dates           BOOLEAN NOT NULL DEFAULT true,
    :entity0_cardinality INTEGER NOT NULL DEFAULT 0,
    :entity1_cardinality INTEGER NOT NULL DEFAULT 0
 :link_type_attribute_type
    :link_type           INTEGER NOT NULL, -- PK, references link_type.id
    :attribute_type      INTEGER NOT NULL, -- PK, references link_attribute_type.id
    :min                 SMALLINT,
    :max                 SMALLINT,
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :editor_collection
;     id                  SERIAL,
;     gid                 UUID NOT NULL,
;     editor              INTEGER NOT NULL, -- references editor.id
;     name                VARCHAR NOT NULL,
;     public              BOOLEAN NOT NULL DEFAULT FALSE,
;     description         TEXT DEFAULT '' NOT NULL,
;     type                INTEGER NOT NULL -- references editor_collection_type.id
 :editor_collection_type
;     id                  SERIAL,
;     name                VARCHAR(255) NOT NULL,
;     entity_type         VARCHAR(50) NOT NULL,
;     parent              INTEGER, -- references editor_collection_type.id
;     child_order         INTEGER NOT NULL DEFAULT 0,
;     description         TEXT,
;     gid                 uuid NOT NULL
 :editor_collection_collaborator
;     collection INTEGER NOT NULL, -- PK, references editor_collection.id
;     editor INTEGER NOT NULL -- PK, references editor.id
 :editor_collection_area
;     collection INTEGER NOT NULL, -- PK, references editor_collection.id
;     area INTEGER NOT NULL, -- PK, references area.id
;     added TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
;     comment TEXT DEFAULT '' NOT NULL
 :editor_collection_artist
;     collection INTEGER NOT NULL, -- PK, references editor_collection.id
;     artist INTEGER NOT NULL, -- PK, references artist.id
;     added TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
;     comment TEXT DEFAULT '' NOT NULL
 :editor_collection_event
;     collection INTEGER NOT NULL, -- PK, references editor_collection.id
;     event INTEGER NOT NULL, -- PK, references event.id
;     added TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
;     comment TEXT DEFAULT '' NOT NULL
 :editor_collection_instrument
;     collection INTEGER NOT NULL, -- PK, references editor_collection.id
;     instrument INTEGER NOT NULL, -- PK, references instrument.id
;     added TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
;     comment TEXT DEFAULT '' NOT NULL
 :editor_collection_label
;     collection INTEGER NOT NULL, -- PK, references editor_collection.id
;     label INTEGER NOT NULL, -- PK, references label.id
;     added TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
;     comment TEXT DEFAULT '' NOT NULL
 :editor_collection_place
;     collection INTEGER NOT NULL, -- PK, references editor_collection.id
;     place INTEGER NOT NULL, -- PK, references place.id
;     added TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
;     comment TEXT DEFAULT '' NOT NULL
 :editor_collection_recording
;     collection INTEGER NOT NULL, -- PK, references editor_collection.id
;     recording INTEGER NOT NULL, -- PK, references recording.id
;     added TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
;     comment TEXT DEFAULT '' NOT NULL
 :editor_collection_release
;     collection INTEGER NOT NULL, -- PK, references editor_collection.id
;     release INTEGER NOT NULL, -- PK, references release.id
;     added TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
;     comment TEXT DEFAULT '' NOT NULL
 :editor_collection_release_group
;     collection INTEGER NOT NULL, -- PK, references editor_collection.id
;     release_group INTEGER NOT NULL, -- PK, references release_group.id
;     added TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
;     comment TEXT DEFAULT '' NOT NULL
 :editor_collection_series
;     collection INTEGER NOT NULL, -- PK, references editor_collection.id
;     series INTEGER NOT NULL, -- PK, references series.id
;     added TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
;     comment TEXT DEFAULT '' NOT NULL
 :editor_collection_work
;     collection INTEGER NOT NULL, -- PK, references editor_collection.id
;     work INTEGER NOT NULL, -- PK, references work.id
;     added TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
;     comment TEXT DEFAULT '' NOT NULL
 :editor_collection_deleted_entity
;     collection INTEGER NOT NULL, -- PK, references editor_collection.id
;     gid UUID NOT NULL, -- PK, references deleted_entity.gid
;     added TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     position INTEGER NOT NULL DEFAULT 0 CHECK (position >= 0),
;     comment TEXT DEFAULT '' NOT NULL
 :editor_oauth_token
;     id                  SERIAL,
;     editor              INTEGER NOT NULL, -- references editor.id
;     application         INTEGER NOT NULL, -- references application.id
;     authorization_code  TEXT,
;     refresh_token       TEXT,
;     access_token        TEXT,
;     expire_time         TIMESTAMP WITH TIME ZONE NOT NULL,
;     scope               INTEGER NOT NULL DEFAULT 0,
;     granted             TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
 :editor_watch_preferences
;     editor INTEGER NOT NULL, -- PK, references editor.id CASCADE
;     notify_via_email BOOLEAN NOT NULL DEFAULT TRUE,
;     notification_timeframe INTERVAL NOT NULL DEFAULT '1 week',
;     last_checked TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
 :editor_watch_artist
;     artist INTEGER NOT NULL, -- PK, references artist.id CASCADE
;     editor INTEGER NOT NULL  -- PK, references editor.id CASCADE
 :editor_watch_release_group_type
;     editor INTEGER NOT NULL, -- PK, references editor.id CASCADE
;     release_group_type INTEGER NOT NULL -- PK, references release_group_primary_type.id
 :editor_watch_release_status
;     editor INTEGER NOT NULL, -- PK, references editor.id CASCADE
;     release_status INTEGER NOT NULL -- PK, references release_status.id
 :medium
;     id                  SERIAL,
;     release             INTEGER NOT NULL, -- references release.id
;     position            INTEGER NOT NULL,
;     format              INTEGER, -- references medium_format.id
;     name                VARCHAR NOT NULL DEFAULT '',
;     edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
;     last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     track_count         INTEGER NOT NULL DEFAULT 0
 :medium_attribute_type
;     id                  SERIAL,  -- PK
;     name                VARCHAR(255) NOT NULL,
;     comment             VARCHAR(255) NOT NULL DEFAULT '',
;     free_text           BOOLEAN NOT NULL,
;     parent              INTEGER, -- references medium_attribute_type.id
;     child_order         INTEGER NOT NULL DEFAULT 0,
;     description         TEXT,
;     gid                 uuid NOT NULL
 :medium_attribute_type_allowed_format
;     medium_format INTEGER NOT NULL, -- PK, references medium_format.id,
;     medium_attribute_type INTEGER NOT NULL -- PK, references medium_attribute_type.id
 :medium_attribute_type_allowed_value
;     id                          SERIAL,  -- PK
;     medium_attribute_type       INTEGER NOT NULL, -- references medium_attribute_type.id
;     value                       TEXT,
;     parent                      INTEGER, -- references medium_attribute_type_allowed_value.id
;     child_order                 INTEGER NOT NULL DEFAULT 0,
;     description                 TEXT,
;     gid                         uuid NOT NULL
 :medium_attribute_type_allowed_value_allowed_format
;     medium_format INTEGER NOT NULL, -- PK, references medium_format.id,
;     medium_attribute_type_allowed_value INTEGER NOT NULL -- PK, references medium_attribute_type_allowed_value.id
 :medium_attribute
;     id                                  SERIAL,  -- PK
;     medium                              INTEGER NOT NULL, -- references medium.id
;     medium_attribute_type               INTEGER NOT NULL, -- references medium_attribute_type.id
;     medium_attribute_type_allowed_value INTEGER, -- references medium_attribute_type_allowed_value.id
;     medium_attribute_text               TEXT
 :medium_cdtoc
;     id                  SERIAL,
;     medium              INTEGER NOT NULL, -- references medium.id
;     cdtoc               INTEGER NOT NULL, -- references cdtoc.id
;     edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
;     last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :medium_format
;     id                  SERIAL,
;     name                VARCHAR(100) NOT NULL,
;     parent              INTEGER, -- references medium_format.id
;     child_order         INTEGER NOT NULL DEFAULT 0,
;     year                SMALLINT,
;     has_discids         BOOLEAN NOT NULL DEFAULT FALSE,
;     description         TEXT,
;     gid                 uuid NOT NULL
 :orderable_link_type
;     link_type           INTEGER NOT NULL, -- PK, references link_type.id
;     direction           SMALLINT NOT NULL DEFAULT 1 CHECK (direction = 1 OR direction = 2)
 :place
;     id                  SERIAL, -- PK
;     gid                 uuid NOT NULL,
;     name                VARCHAR NOT NULL,
;     type                INTEGER, -- references place_type.id
;     address             VARCHAR NOT NULL DEFAULT '',
;     area                INTEGER, -- references area.id
;     coordinates         POINT,
;     comment             VARCHAR(255) NOT NULL DEFAULT '',
;     edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >=0),
;     last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     begin_date_year     SMALLINT,
;     begin_date_month    SMALLINT,
;     begin_date_day      SMALLINT,
;     end_date_year       SMALLINT,
;     end_date_month      SMALLINT,
;     end_date_day        SMALLINT,
;     ended               BOOLEAN NOT NULL DEFAULT FALSE
 :place_alias
;     id                  SERIAL,
;     place               INTEGER NOT NULL, -- references place.id
;     name                VARCHAR NOT NULL,
;     locale              TEXT,
;     edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
;     last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     type                INTEGER, -- references place_alias_type.id
;     sort_name           VARCHAR NOT NULL,
;     begin_date_year     SMALLINT,
;     begin_date_month    SMALLINT,
;     begin_date_day      SMALLINT,
;     end_date_year       SMALLINT,
;     end_date_month      SMALLINT,
;     end_date_day        SMALLINT,
;     primary_for_locale  BOOLEAN NOT NULL DEFAULT false,
;     ended               BOOLEAN NOT NULL DEFAULT FALSE
 :place_alias_type
;     id                  SERIAL,
;     name                TEXT NOT NULL,
;     parent              INTEGER, -- references place_alias_type.id
;     child_order         INTEGER NOT NULL DEFAULT 0,
;     description         TEXT,
;     gid                 uuid NOT NULL
 :place_annotation
;     place               INTEGER NOT NULL, -- PK, references place.id
;     annotation          INTEGER NOT NULL -- PK, references annotation.id
 :place_attribute_type
;     id                  SERIAL,  -- PK
;     name                VARCHAR(255) NOT NULL,
;     comment             VARCHAR(255) NOT NULL DEFAULT '',
;     free_text           BOOLEAN NOT NULL,
;     parent              INTEGER, -- references place_attribute_type.id
;     child_order         INTEGER NOT NULL DEFAULT 0,
;     description         TEXT,
;     gid                 uuid NOT NULL
 :place_attribute_type_allowed_value
;     id                          SERIAL,  -- PK
;     place_attribute_type        INTEGER NOT NULL, -- references place_attribute_type.id
;     value                       TEXT,
;     parent                      INTEGER, -- references place_attribute_type_allowed_value.id
;     child_order                 INTEGER NOT NULL DEFAULT 0,
;     description                 TEXT,
;     gid                         uuid NOT NULL
 :place_attribute
;     id                                  SERIAL,  -- PK
;     place                               INTEGER NOT NULL, -- references place.id
;     place_attribute_type                INTEGER NOT NULL, -- references place_attribute_type.id
;     place_attribute_type_allowed_value  INTEGER, -- references place_attribute_type_allowed_value.id
;     place_attribute_text                TEXT
 :place_gid_redirect
;     gid                 UUID NOT NULL, -- PK
;     new_id              INTEGER NOT NULL, -- references place.id
;     created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :place_tag
;     place               INTEGER NOT NULL, -- PK, references place.id
;     tag                 INTEGER NOT NULL, -- PK, references tag.id
;     count               INTEGER NOT NULL,
;     last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :place_tag_raw
;     place               INTEGER NOT NULL, -- PK, references place.id
;     editor              INTEGER NOT NULL, -- PK, references editor.id
;     tag                 INTEGER NOT NULL, -- PK, references tag.id
;     is_upvote           BOOLEAN NOT NULL DEFAULT TRUE
 :place_type
;     id                  SERIAL, -- PK
;     name                VARCHAR(255) NOT NULL,
;     parent              INTEGER, -- references place_type.id
;     child_order         INTEGER NOT NULL DEFAULT 0,
;     description         TEXT,
;     gid                 uuid NOT NULL
 :replication_control
;     id                              SERIAL,
;     current_schema_sequence         INTEGER NOT NULL,
;     current_replication_sequence    INTEGER,
;     last_replication_date           TIMESTAMP WITH TIME ZONE
 :recording
;     id                  SERIAL,
;     gid                 UUID NOT NULL,
;     name                VARCHAR NOT NULL,
;     artist_credit       INTEGER NOT NULL, -- references artist_credit.id
;     length              INTEGER CHECK (length IS NULL OR length > 0),
;     comment             VARCHAR(255) NOT NULL DEFAULT '',
;     edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
;     last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     video               BOOLEAN NOT NULL DEFAULT FALSE
 :recording_alias_type
;     id                  SERIAL, -- PK,
;     name                TEXT NOT NULL,
;     parent              INTEGER, -- references recording_alias_type.id
;     child_order         INTEGER NOT NULL DEFAULT 0,
;     description         TEXT,
;     gid                 uuid NOT NULL
 :recording_alias
;     id                  SERIAL, --PK
;     recording           INTEGER NOT NULL, -- references recording.id
;     name                VARCHAR NOT NULL,
;     locale              TEXT,
;     edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >=0),
;     last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     type                INTEGER, -- references recording_alias_type.id
;     sort_name           VARCHAR NOT NULL,
;     begin_date_year     SMALLINT,
;     begin_date_month    SMALLINT,
;     begin_date_day      SMALLINT,
;     end_date_year       SMALLINT,
;     end_date_month      SMALLINT,
;     end_date_day        SMALLINT,
;     primary_for_locale  BOOLEAN NOT NULL DEFAULT false,
;     ended               BOOLEAN NOT NULL DEFAULT FALSE
 :recording_rating_raw
;     recording           INTEGER NOT NULL, -- PK, references recording.id
;     editor              INTEGER NOT NULL, -- PK, references editor.id
;     rating              SMALLINT NOT NULL CHECK (rating >= 0 AND rating <= 100)
 :recording_tag_raw
;     recording           INTEGER NOT NULL, -- PK, references recording.id
;     editor              INTEGER NOT NULL, -- PK, references editor.id
;     tag                 INTEGER NOT NULL, -- PK, references tag.id
;     is_upvote           BOOLEAN NOT NULL DEFAULT TRUE
 :recording_annotation
;     recording           INTEGER NOT NULL, -- PK, references recording.id
;     annotation          INTEGER NOT NULL -- PK, references annotation.id
 :recording_attribute_type
;     id                  SERIAL,  -- PK
;     name                VARCHAR(255) NOT NULL,
;     comment             VARCHAR(255) NOT NULL DEFAULT '',
;     free_text           BOOLEAN NOT NULL,
;     parent              INTEGER, -- references recording_attribute_type.id
;     child_order         INTEGER NOT NULL DEFAULT 0,
;     description         TEXT,
;     gid                 uuid NOT NULL
 :recording_attribute_type_allowed_value
;     id                          SERIAL,  -- PK
;     recording_attribute_type    INTEGER NOT NULL, -- references recording_attribute_type.id
;     value                       TEXT,
;     parent                      INTEGER, -- references recording_attribute_type_allowed_value.id
;     child_order                 INTEGER NOT NULL DEFAULT 0,
;     description                 TEXT,
;     gid                         uuid NOT NULL
 :recording_attribute
;     id                                          SERIAL,  -- PK
;     recording                                   INTEGER NOT NULL, -- references recording.id
;     recording_attribute_type                    INTEGER NOT NULL, -- references recording_attribute_type.id
;     recording_attribute_type_allowed_value      INTEGER, -- references recording_attribute_type_allowed_value.id
;     recording_attribute_text                    TEXT
 :recording_meta
;     id                  INTEGER NOT NULL, -- PK, references recording.id CASCADE
;     rating              SMALLINT CHECK (rating >= 0 AND rating <= 100),
;     rating_count        INTEGER
 :recording_gid_redirect
;     gid                 UUID NOT NULL, -- PK
;     new_id              INTEGER NOT NULL, -- references recording.id
;     created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :recording_tag
;     recording           INTEGER NOT NULL, -- PK, references recording.id
;     tag                 INTEGER NOT NULL, -- PK, references tag.id
;     count               INTEGER NOT NULL,
;     last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :release
    id                  SERIAL,
    gid                 UUID NOT NULL,
    name                VARCHAR NOT NULL,
    artist_credit       INTEGER NOT NULL, -- references artist_credit.id
    release_group       INTEGER NOT NULL, -- references release_group.id
    status              INTEGER, -- references release_status.id
    packaging           INTEGER, -- references release_packaging.id
    language            INTEGER, -- references language.id
    script              INTEGER, -- references script.id
    barcode             VARCHAR(255),
    comment             VARCHAR(255) NOT NULL DEFAULT '',
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    quality             SMALLINT NOT NULL DEFAULT -1,
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :release_alias_type
;     id                  SERIAL, -- PK,
;     name                TEXT NOT NULL,
;     parent              INTEGER, -- references release_alias_type.id
;     child_order         INTEGER NOT NULL DEFAULT 0,
;     description         TEXT,
;     gid                 uuid NOT NULL
 :release_alias
    id                  SERIAL, --PK
    release             INTEGER NOT NULL, -- references release.id
    name                VARCHAR NOT NULL,
    locale              TEXT,
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >=0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    type                INTEGER, -- references release_alias_type.id
    sort_name           VARCHAR NOT NULL,
    begin_date_year     SMALLINT,
    begin_date_month    SMALLINT,
    begin_date_day      SMALLINT,
    end_date_year       SMALLINT,
    end_date_month      SMALLINT,
    end_date_day        SMALLINT,
    primary_for_locale  BOOLEAN NOT NULL DEFAULT false,
 :release_country
;   release INTEGER NOT NULL,  -- PK, references release.id
;   country INTEGER NOT NULL,  -- PK, references country_area.area
;   date_year SMALLINT,
;   date_month SMALLINT,
;   date_day SMALLINT
 :release_unknown_country
;   release INTEGER NOT NULL,  -- PK, references release.id
;   date_year SMALLINT,
;   date_month SMALLINT,
;   date_day SMALLINT
 :release_raw
;     id                  SERIAL, -- PK
;     title               VARCHAR(255) NOT NULL,
;     artist              VARCHAR(255),
;     added               TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     last_modified        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     lookup_count         INTEGER DEFAULT 0,
;     modify_count         INTEGER DEFAULT 0,
;     source              INTEGER DEFAULT 0,
;     barcode             VARCHAR(255),
;     comment             VARCHAR(255) NOT NULL DEFAULT ''
 :release_tag_raw
;     release             INTEGER NOT NULL, -- PK, references release.id
;     editor              INTEGER NOT NULL, -- PK, references editor.id
;     tag                 INTEGER NOT NULL, -- PK, references tag.id
;     is_upvote           BOOLEAN NOT NULL DEFAULT TRUE
 :release_annotation
    release             INTEGER NOT NULL, -- PK, references release.id
    annotation          INTEGER NOT NULL -- PK, references annotation.id
 :release_attribute_type
    id                  SERIAL,  -- PK
    name                VARCHAR(255) NOT NULL,
    comment             VARCHAR(255) NOT NULL DEFAULT '',
    free_text           BOOLEAN NOT NULL,
    parent              INTEGER, -- references release_attribute_type.id
    child_order         INTEGER NOT NULL DEFAULT 0,
    description         TEXT,
    gid                 uuid NOT NULL
 :release_attribute_type_allowed_value
    id                          SERIAL,  -- PK
    release_attribute_type      INTEGER NOT NULL, -- references release_attribute_type.id
    value                       TEXT,
    parent                      INTEGER, -- references release_attribute_type_allowed_value.id
    child_order                 INTEGER NOT NULL DEFAULT 0,
    description                 TEXT,
    gid                         uuid NOT NULL
 :release_attribute
    id                                          SERIAL,  -- PK
    release                                     INTEGER NOT NULL, -- references release.id
    release_attribute_type                      INTEGER NOT NULL, -- references release_attribute_type.id
    release_attribute_type_allowed_value        INTEGER, -- references release_attribute_type_allowed_value.id
    release_attribute_text                      TEXT
 :release_gid_redirect
    gid                 UUID NOT NULL, -- PK
    new_id              INTEGER NOT NULL, -- references release.id
    created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
; CREATE TYPE cover_art_presence AS ENUM ('absent', 'present', 'darkened');
 :release_meta
    id                  INTEGER NOT NULL, -- PK, references release.id CASCADE
    date_added          TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    info_url            VARCHAR(255),
    amazon_asin         VARCHAR(10),
    amazon_store        VARCHAR(20),
    cover_art_presence  cover_art_presence NOT NULL DEFAULT 'absent'
 :release_coverart
    id                  INTEGER NOT NULL, -- PK, references release.id CASCADE
    last_updated        TIMESTAMP WITH TIME ZONE,
    cover_art_url       VARCHAR(255)
 :release_label
    id                  SERIAL,
    release             INTEGER NOT NULL, -- references release.id
    label               INTEGER, -- references label.id
    catalog_number      VARCHAR(255),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :release_packaging
    id                  SERIAL,
    name                VARCHAR(255) NOT NULL,
    parent              INTEGER, -- references release_packaging.id
    child_order         INTEGER NOT NULL DEFAULT 0,
    description         TEXT,
    gid                 uuid NOT NULL
 :release_status
    id                  SERIAL,
    name                VARCHAR(255) NOT NULL,
    parent              INTEGER, -- references release_status.id
    child_order         INTEGER NOT NULL DEFAULT 0,
    description         TEXT,
    gid                 uuid NOT NULL
 :release_tag
    release             INTEGER NOT NULL, -- PK, references release.id
    tag                 INTEGER NOT NULL, -- PK, references tag.id
    count               INTEGER NOT NULL,
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :release_group
    id                  SERIAL,
    gid                 UUID NOT NULL,
    name                VARCHAR NOT NULL,
    artist_credit       INTEGER NOT NULL, -- references artist_credit.id
    type                INTEGER, -- references release_group_primary_type.id
    comment             VARCHAR(255) NOT NULL DEFAULT '',
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :release_group_alias_type
    id                  SERIAL, -- PK,
    name                TEXT NOT NULL,
    parent              INTEGER, -- references release_group_alias_type.id
    child_order         INTEGER NOT NULL DEFAULT 0,
    description         TEXT,
    gid                 uuid NOT NULL
 :release_group_alias
;     id                  SERIAL, --PK
;     release_group       INTEGER NOT NULL, -- references release_group.id
;     name                VARCHAR NOT NULL,
;     locale              TEXT,
;     edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >=0),
;     last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     type                INTEGER, -- references release_group_alias_type.id
;     sort_name           VARCHAR NOT NULL,
;     begin_date_year     SMALLINT,
;     begin_date_month    SMALLINT,
;     begin_date_day      SMALLINT,
;     end_date_year       SMALLINT,
;     end_date_month      SMALLINT,
;     end_date_day        SMALLINT,
;     primary_for_locale  BOOLEAN NOT NULL DEFAULT false,
 :release_group_rating_raw
;     release_group       INTEGER NOT NULL, -- PK, references release_group.id
;     editor              INTEGER NOT NULL, -- PK, references editor.id
;     rating              SMALLINT NOT NULL CHECK (rating >= 0 AND rating <= 100)
 :release_group_tag_raw
;     release_group       INTEGER NOT NULL, -- PK, references release_group.id
;     editor              INTEGER NOT NULL, -- PK, references editor.id
;     tag                 INTEGER NOT NULL, -- PK, references tag.id
;     is_upvote           BOOLEAN NOT NULL DEFAULT TRUE
 :release_group_annotation
;     release_group       INTEGER NOT NULL, -- PK, references release_group.id
;     annotation          INTEGER NOT NULL -- PK, references annotation.id
 :release_group_attribute_type
;     id                  SERIAL,  -- PK
;     name                VARCHAR(255) NOT NULL,
;     comment             VARCHAR(255) NOT NULL DEFAULT '',
;     free_text           BOOLEAN NOT NULL,
;     parent              INTEGER, -- references release_group_attribute_type.id
;     child_order         INTEGER NOT NULL DEFAULT 0,
;     description         TEXT,
;     gid                 uuid NOT NULL
 :release_group_attribute_type_allowed_value
;     id                                  SERIAL,  -- PK
;     release_group_attribute_type        INTEGER NOT NULL, -- references release_group_attribute_type.id
;     value                               TEXT,
;     parent                              INTEGER, -- references release_group_attribute_type_allowed_value.id
;     child_order                         INTEGER NOT NULL DEFAULT 0,
;     description                         TEXT,
;     gid                                 uuid NOT NULL
 :release_group_attribute
;     id                                          SERIAL,  -- PK
;     release_group                               INTEGER NOT NULL, -- references release_group.id
;     release_group_attribute_type                INTEGER NOT NULL, -- references release_group_attribute_type.id
;     release_group_attribute_type_allowed_value  INTEGER, -- references release_group_attribute_type_allowed_value.id
;     release_group_attribute_text                TEXT
 :release_group_gid_redirect
;     gid                 UUID NOT NULL, -- PK
;     new_id              INTEGER NOT NULL, -- references release_group.id
;     created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :release_group_meta
;     id                  INTEGER NOT NULL, -- PK, references release_group.id CASCADE
;     release_count       INTEGER NOT NULL DEFAULT 0,
;     first_release_date_year   SMALLINT,
;     first_release_date_month  SMALLINT,
;     first_release_date_day    SMALLINT,
;     rating              SMALLINT CHECK (rating >= 0 AND rating <= 100),
;     rating_count        INTEGER
 :release_group_tag
;     release_group       INTEGER NOT NULL, -- PK, references release_group.id
;     tag                 INTEGER NOT NULL, -- PK, references tag.id
;     count               INTEGER NOT NULL,
;     last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :release_group_primary_type
;     id                  SERIAL,
;     name                VARCHAR(255) NOT NULL,
;     parent              INTEGER, -- references release_group_primary_type.id
;     child_order         INTEGER NOT NULL DEFAULT 0,
;     description         TEXT,
;     gid                 uuid NOT NULL
 :release_group_secondary_type
;     id                  SERIAL NOT NULL, -- PK
;     name                TEXT NOT NULL,
;     parent              INTEGER, -- references release_group_secondary_type.id
;     child_order         INTEGER NOT NULL DEFAULT 0,
;     description         TEXT,
;     gid                 uuid NOT NULL
 :release_group_secondary_type_join
    release_group       INTEGER NOT NULL, -- PK, references release_group.id,
    secondary_type      INTEGER NOT NULL, -- PK, references release_group_secondary_type.id
    created             TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
 :script
    id                  SERIAL,
    iso_code            CHAR(4) NOT NULL, -- ISO 15924
    iso_number          CHAR(3) NOT NULL, -- ISO 15924
    name                VARCHAR(100) NOT NULL,
    frequency           INTEGER NOT NULL DEFAULT 0
 :series
    id                  SERIAL,
    gid                 UUID NOT NULL,
    name                VARCHAR NOT NULL,
    comment             VARCHAR(255) NOT NULL DEFAULT '',
    type                INTEGER NOT NULL, -- references series_type.id
    ordering_attribute  INTEGER NOT NULL, -- references link_text_attribute_type.attribute_type
    ordering_type       INTEGER NOT NULL, -- references series_ordering_type.id
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :series_type
    id                  SERIAL,
    name                VARCHAR(255) NOT NULL,
    entity_type         VARCHAR(50) NOT NULL,
    parent              INTEGER, -- references series_type.id
    child_order         INTEGER NOT NULL DEFAULT 0,
    description         TEXT,
    gid                 uuid NOT NULL
 :series_ordering_type
    id                  SERIAL,
    name                VARCHAR(255) NOT NULL,
    parent              INTEGER, -- references series_ordering_type.id
    child_order         INTEGER NOT NULL DEFAULT 0,
    description         TEXT,
    gid                 uuid NOT NULL
 :series_gid_redirect
    gid                 UUID NOT NULL, -- PK
    new_id              INTEGER NOT NULL, -- references series.id
    created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :series_alias_type
    id                  SERIAL, -- PK
    name                TEXT NOT NULL,
    parent              INTEGER, -- references series_alias_type.id
    child_order         INTEGER NOT NULL DEFAULT 0,
    description         TEXT,
    gid                 uuid NOT NULL
 :series_alias
    id                  SERIAL, -- PK
    series              INTEGER NOT NULL, -- references series.id
    name                VARCHAR NOT NULL,
    locale              TEXT,
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    type                INTEGER, -- references series_alias_type.id
    sort_name           VARCHAR NOT NULL,
    begin_date_year     SMALLINT,
    begin_date_month    SMALLINT,
    begin_date_day      SMALLINT,
    end_date_year       SMALLINT,
    end_date_month      SMALLINT,
    end_date_day        SMALLINT,
    primary_for_locale  BOOLEAN NOT NULL DEFAULT FALSE,
    ended               BOOLEAN NOT NULL DEFAULT FALSE
 :series_annotation
    series              INTEGER NOT NULL, -- PK, references series.id
    annotation          INTEGER NOT NULL -- PK, references annotation.id
 :series_attribute_type
    id                  SERIAL,  -- PK
    name                VARCHAR(255) NOT NULL,
    comment             VARCHAR(255) NOT NULL DEFAULT '',
    free_text           BOOLEAN NOT NULL,
    parent              INTEGER, -- references series_attribute_type.id
    child_order         INTEGER NOT NULL DEFAULT 0,
    description         TEXT,
    gid                 uuid NOT NULL
 :series_attribute_type_allowed_value
    id                          SERIAL,  -- PK
    series_attribute_type       INTEGER NOT NULL, -- references series_attribute_type.id
    value                       TEXT,
    parent                      INTEGER, -- references series_attribute_type_allowed_value.id
    child_order                 INTEGER NOT NULL DEFAULT 0,
    description                 TEXT,
    gid                         uuid NOT NULL
 :series_attribute
    id                                  SERIAL,  -- PK
    series                              INTEGER NOT NULL, -- references series.id
    series_attribute_type               INTEGER NOT NULL, -- references series_attribute_type.id
    series_attribute_type_allowed_value INTEGER, -- references series_attribute_type_allowed_value.id
    series_attribute_text               TEXT
 :series_tag
    series              INTEGER NOT NULL, -- PK, references series.id
    tag                 INTEGER NOT NULL, -- PK, references tag.id
    count               INTEGER NOT NULL,
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :series_tag_raw
    series              INTEGER NOT NULL, -- PK, references series.id
    editor              INTEGER NOT NULL, -- PK, references editor.id
    tag                 INTEGER NOT NULL, -- PK, references tag.id
    is_upvote           BOOLEAN NOT NULL DEFAULT TRUE
 :tag
    id                  SERIAL,
    name                VARCHAR(255) NOT NULL,
    ref_count           INTEGER NOT NULL DEFAULT 0
 :tag_relation
    tag1                INTEGER NOT NULL, -- PK, references tag.id
    tag2                INTEGER NOT NULL, -- PK, references tag.id
    weight              INTEGER NOT NULL,
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CHECK (tag1 < tag2)
 :track
    id                  SERIAL,
    gid                 UUID NOT NULL,
    recording           INTEGER NOT NULL, -- references recording.id
    medium              INTEGER NOT NULL, -- references medium.id
    position            INTEGER NOT NULL,
    number              TEXT NOT NULL,
    name                VARCHAR NOT NULL,
    artist_credit       INTEGER NOT NULL, -- references artist_credit.id
    length              INTEGER CHECK (length IS NULL OR length > 0),
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_data_track       BOOLEAN NOT NULL DEFAULT FALSE
 :track_gid_redirect
    gid                 UUID NOT NULL, -- PK
    new_id              INTEGER NOT NULL, -- references track.id
    created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :track_raw
    id                  SERIAL, -- PK
    release             INTEGER NOT NULL,   -- references release_raw.id
    title               VARCHAR(255) NOT NULL,
    artist              VARCHAR(255),   -- For VA albums, otherwise empty
    sequence            INTEGER NOT NULL
 :medium_index
    medium              INTEGER, -- PK, references medium.id CASCADE
    toc                 CUBE
 :url
    id                  SERIAL,
    gid                 UUID NOT NULL,
    url                 TEXT NOT NULL,
    edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :url_gid_redirect
;     gid                 UUID NOT NULL, -- PK
;     new_id              INTEGER NOT NULL, -- references url.id
;     created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :vote
;     id                  SERIAL,
;     editor              INTEGER NOT NULL, -- references editor.id
;     edit                INTEGER NOT NULL, -- references edit.id
;     vote                SMALLINT NOT NULL,
;     vote_time            TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
;     superseded          BOOLEAN NOT NULL DEFAULT FALSE
 :work
;     id                  SERIAL,
;     gid                 UUID NOT NULL,
;     name                VARCHAR NOT NULL,
;     type                INTEGER, -- references work_type.id
;     comment             VARCHAR(255) NOT NULL DEFAULT '',
;     edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
;     last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :work_language
;     work                INTEGER NOT NULL, -- PK, references work.id
;     language            INTEGER NOT NULL, -- PK, references language.id
;     edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
;     created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :work_rating_raw
;     work                INTEGER NOT NULL, -- PK, references work.id
;     editor              INTEGER NOT NULL, -- PK, references editor.id
;     rating              SMALLINT NOT NULL CHECK (rating >= 0 AND rating <= 100)
 :work_tag_raw
;     work                INTEGER NOT NULL, -- PK, references work.id
;     editor              INTEGER NOT NULL, -- PK, references editor.id
;     tag                 INTEGER NOT NULL, -- PK, references tag.id
;     is_upvote           BOOLEAN NOT NULL DEFAULT TRUE
 :work_alias_type
;     id                  SERIAL,
;     name                TEXT NOT NULL,
;     parent              INTEGER, -- references work_alias_type.id
;     child_order         INTEGER NOT NULL DEFAULT 0,
;     description         TEXT,
;     gid                 uuid NOT NULL
 :work_alias
    :id                  SERIAL,
    :work                INTEGER NOT NULL, -- references work.id
    :name                VARCHAR NOT NULL,
    :locale              TEXT,
    :edits_pending       INTEGER NOT NULL DEFAULT 0 CHECK (edits_pending >= 0),
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    :type                INTEGER, -- references work_alias_type.id
    :sort_name           VARCHAR NOT NULL,
    :begin_date_year     SMALLINT,
    :begin_date_month    SMALLINT,
    :begin_date_day      SMALLINT,
    :end_date_year       SMALLINT,
    :end_date_month      SMALLINT,
    end_date_day        SMALLINT,
    primary_for_locale  BOOLEAN NOT NULL DEFAULT false,
    ended               BOOLEAN NOT NULL DEFAULT FALSE
 :work_annotation
    :work                INTEGER NOT NULL, -- PK, references work.id
    :annotation          INTEGER NOT NULL -- PK, references annotation.id
 :work_gid_redirect
    :gid                 UUID NOT NULL, -- PK
    :new_id              INTEGER NOT NULL, -- references work.id
    :created             TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :work_meta
    :id                  INTEGER NOT NULL, -- PK, references work.id CASCADE
    :rating              SMALLINT CHECK (rating >= 0 AND rating <= 100),
    :rating_count        INTEGER
 :work_tag
    :work                INTEGER NOT NULL, -- PK, references work.id
    :tag                 INTEGER NOT NULL, -- PK, references tag.id
    :count               INTEGER NOT NULL,
    :last_updated        TIMESTAMP WITH TIME ZONE DEFAULT NOW()
 :work_type
    :id                  SERIAL,
    :name                VARCHAR(255) NOT NULL,
    :parent              INTEGER, -- references work_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :work_attribute_type
    :id                  SERIAL,  -- PK
    :name                VARCHAR(255) NOT NULL,
    :comment             VARCHAR(255) NOT NULL DEFAULT '',
    :free_text           BOOLEAN NOT NULL,
    :parent              INTEGER, -- references work_attribute_type.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :work_attribute_type_allowed_value
    :id                  SERIAL,  -- PK
    :work_attribute_type INTEGER NOT NULL, -- references work_attribute_type.id
    :value               TEXT,
    :parent              INTEGER, -- references work_attribute_type_allowed_value.id
    :child_order         INTEGER NOT NULL DEFAULT 0,
    :description         TEXT,
    :gid                 uuid NOT NULL
 :work_attribute
    :id                                  SERIAL,  -- PK
    :work                                INTEGER NOT NULL, -- references work.id
    :work_attribute_type                 INTEGER NOT NULL, -- references work_attribute_type.id
    :work_attribute_type_allowed_value   INTEGER, -- references work_attribute_type_allowed_value.id
    :work_attribute_text                 TEXT
})
