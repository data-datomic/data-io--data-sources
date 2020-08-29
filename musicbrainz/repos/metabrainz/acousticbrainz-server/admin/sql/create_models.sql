BEGIN;

INSERT INTO model (model, model_version, date, status) VALUES ('danceability', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('gender', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('genre_dortmund', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('genre_electronic', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('genre_rosamerica', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('genre_tzanetakis', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('ismir04_rhythm', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('mood_acoustic', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('mood_aggressive', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('mood_electronic', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('mood_happy', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('mood_party', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('mood_relaxed', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('mood_sad', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('moods_mirex', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('timbre', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('tonal_atonal', 'v2.1_beta1', now(), 'show');
INSERT INTO model (model, model_version, date, status) VALUES ('voice_instrumental', 'v2.1_beta1', now(), 'show');

UPDATE model SET class_mapping = '{
        "danceable": "Danceable",
        "not_danceable": "Not danceable"
    }'::jsonb WHERE model.model = 'danceability';

UPDATE model SET class_mapping = '{
        "acoustic": "Acoustic",
        "not_acoustic": "Not acoustic"
    }'::jsonb WHERE model.model = 'mood_acoustic';

UPDATE model SET class_mapping = '{
        "aggressive": "Aggressive",
        "not_aggressive": "Not aggressive"
    }'::jsonb WHERE model.model = 'mood_aggressive';

UPDATE model SET class_mapping = '{
        "electronic": "Electronic",
        "not_electronic": "Not electronic"
    }'::jsonb WHERE model.model = 'mood_electronic';

UPDATE model SET class_mapping = '{
        "happy": "Happy",
        "not_happy": "Not happy"
    }'::jsonb WHERE model.model = 'mood_happy';

UPDATE model SET class_mapping = '{
        "party": "Party",
        "not_party": "Not party"
    }'::jsonb WHERE model.model = 'mood_party';

UPDATE model SET class_mapping = '{
        "relaxed": "Relaxed",
        "not_relaxed": "Not relaxed"
    }'::jsonb WHERE model.model = 'mood_relaxed';

UPDATE model SET class_mapping = '{
        "sad": "Sad",
        "not_sad": "Not sad"
    }'::jsonb WHERE model.model = 'mood_sad';

UPDATE model SET class_mapping = '{
        "Cluster1": "passionate, rousing, confident, boisterous, rowdy",
        "Cluster2": "rollicking, cheerful, fun, sweet, amiable/good natured",
        "Cluster3": "literate, poignant, wistful, bittersweet, autumnal, brooding",
        "Cluster4": "humorous, silly, campy, quirky, whimsical, witty, wry",
        "Cluster5": "aggressive, fiery, tense/anxious, intense, volatile, visceral"
    }'::jsonb WHERE model.model = 'moods_mirex';

UPDATE model SET class_mapping = '{
        "alternative": "Alternative",
        "blues": "Blues",
        "electronic": "Electronic",
        "folkcountry": "Folk/Country",
        "funksoulrnb": "Funk/Soul/RnB",
        "jazz": "Jazz",
        "pop": "Pop",
        "raphiphop": "Rap/Hiphop",
        "rock": "Rock"
    }'::jsonb WHERE model.model = 'genre_dortmund';

UPDATE model SET class_mapping = '{
        "ambient": "Ambient",
        "dnb": "Drum and Bass",
        "house": "House",
        "techno": "Techno",
        "trance": "Trance"
    }'::jsonb WHERE model.model = 'genre_electronic';

UPDATE model SET class_mapping = '{
        "cla": "Classical",
        "dan": "Dance",
        "hip": "Hiphop",
        "jaz": "Jazz",
        "pop": "Pop",
        "rhy": "Rhythm and Blues",
        "roc": "Rock",
        "spe": "Speech"
    }'::jsonb WHERE model.model = 'genre_rosamerica';

UPDATE model SET class_mapping = '{
        "blu": "Blues",
        "cla": "Classical",
        "cou": "Country",
        "dis": "Disco",
        "hip": "Hiphop",
        "jaz": "Jazz",
        "met": "Metal",
        "pop": "Pop",
        "reg": "Reggae",
        "roc": "Rock"
    }'::jsonb WHERE model.model = 'genre_tzanetakis';

COMMIT;
