.PHONY: all clean

all: mbrng/__init__.py mbrng/models.py

mbrng/__init__.py:
	python3 setup.py version

mbrng/models.py: musicbrainz_mmd.xsd
	generateDS.py -o $@ -s mbrng/mb_mmd_subs.py --super="mb" --external-encoding="utf-8" --export="write etree" musicbrainz_mmd.xsd

musicbrainz_mmd.xsd: mmd-schema/schema/musicbrainz_mmd-2.0.rng
	trang mmd-schema/schema/musicbrainz_mmd-2.0.rng $@

clean:
	rm mbrng/models.py mbrng/mb_mmd_subs.py musicbrainz_mmd.xsd
