MB_INDEXES := artist releasegroup release recording label cdstub tag work annotation
#MB_INDEXES := artist releasegroup recording label cdstub tag work annotation

#JAR:= jar/index-2.0-SNAPSHOT-jar-with-dependencies.20110522.jar
JAR:= jar/index-2.0-SNAPSHOT-jar-with-dependencies.20110607.jar

.PHONY: default mb $(MB_INDEXES) freedb rotate

default: freedb mb

mb: build_all

mb_index:
	mkdir -p ./data/tmp
	rm -rf ./data/tmp/$(TYPE)_index
	env JAR=$(JAR) builder --indexes-dir ./data/tmp --freedb-dump freedb-data/freedb-complete-latest.tar.bz2 --indexes $(TYPE)
	$(MAKE) rotate INDEX=$(TYPE)_index

build_all: 
	mkdir -p ./data/tmp
	rm -rf ./data/tmp/artist_index ./data/tmp/releasegroup_index ./data/tmp/release_index ./data/tmp/recording_index ./data/tmp/label_index ./data/tmp/cdstub_index ./data/tmp/tag_index ./data/tmp/work_index ./data/tmp/annotation_index
	env JAR=$(JAR) builder --indexes-dir ./data/tmp --indexes artist,releasegroup,release,recording,label,cdstub,tag,work,annotation
	$(MAKE) rotate INDEX=artist_index
	$(MAKE) rotate INDEX=releasegroup_index
	$(MAKE) rotate INDEX=release_index
	$(MAKE) rotate INDEX=recording_index
	$(MAKE) rotate INDEX=label_index
	$(MAKE) rotate INDEX=cdstub_index
	$(MAKE) rotate INDEX=tag_index
	$(MAKE) rotate INDEX=work_index
	$(MAKE) rotate INDEX=annotation_index

rotate:
	[ "$(INDEX)" ]
	chmod 755 ./data/tmp/$(INDEX)
	chmod 644 ./data/tmp/$(INDEX)/*
	mkdir -m 755 -p ./data/old ./data/cur
	rm -rf ./data/old/$(INDEX).1
	if [ -e ./data/old/$(INDEX).0 ] ; then mv ./data/old/$(INDEX).0 ./data/old/$(INDEX).1 ; fi
	if [ -e ./data/cur/$(INDEX) ]   ; then mv ./data/cur/$(INDEX)   ./data/old/$(INDEX).0 ; fi
	mv -v ./data/tmp/$(INDEX) ./data/cur/$(INDEX)
	rm -rf ./data/old/$(INDEX).1

artist: $(JAR)
	$(MAKE) mb_index TYPE=artist

releasegroup: $(JAR)
	$(MAKE) mb_index TYPE=releasegroup

release: $(JAR)
	$(MAKE) mb_index TYPE=release

recording: $(JAR)
	$(MAKE) mb_index TYPE=recording

label: $(JAR)
	$(MAKE) mb_index TYPE=label

annotation: $(JAR)
	$(MAKE) mb_index TYPE=annotation

tag: $(JAR)
	$(MAKE) mb_index TYPE=tag

work: $(JAR)
	$(MAKE) mb_index TYPE=work

cdstub: $(JAR)
	$(MAKE) mb_index TYPE=cdstub

freedb: data/cur/freedb

data/cur/freedb: freedb-data/freedb-complete-latest.tar.bz2 $(JAR)
	$(MAKE) mb_index TYPE=freedb

