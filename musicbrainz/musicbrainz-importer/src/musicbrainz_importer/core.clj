(ns musicbrainz-importer.core
  (:require
    [clojure.pprint :as pp]
    [clojure.string :as cs]
    [musicbrainz-importer.mbserver-table-defs :as mbtab])
  (:gen-class))

(def sepchar \	)

(defn read-linez [tbl-key line-seq]
  (doseq [line line-seq]
    (println "line: " line)))

  ; (let [dat-vals (cs/split sepchar line)]
  ;
  ; )

; (defn read-file [file]
;   (with-open [rdr (clojure.java.io/reader file)]
;      (line-seq rdr))
;
;   ; (let [contents (slurp f)]
;   ;    contents
;   )

(defn find-nested
  [m k]
  (->> (tree-seq map? vals m)
       (filter map?)
       (some k)))

(defn process-mbdump [dump-dir]
  (let [md-dir-path (str (.getAbsolutePath dump-dir) "/mbdump")
        md-dir (clojure.java.io/file md-dir-path)
        files (filter #(not (= (.getAbsolutePath %) md-dir-path)) (file-seq md-dir))
        tf-map (into {} (map (fn [f]
                               (let [file-name (.getName f)
                                     filekw (keyword file-name)
                                     file-path (.getAbsolutePath f)]
                                 {filekw file-path})) files))]
    tf-map))

(defn build-table-file-map [root-dir-path]
  (let [root-f (clojure.java.io/file root-dir-path)
        fc (file-seq root-f)
        table-file-map (atom {})]
    (doseq [f fc]
      (let [child-count (count (flatten (filter (fn [f]
                            (and (.isDirectory f)
                                 (= (.getName f) "mbdump")))
                          (.listFiles f))))
            is-dir (.isDirectory f)
            starts-with-mdump (or (cs/starts-with? (.getName f) "mbdump-")
                                  (= (.getName f) "mbdump"))
            is-mbdump-archive (and
                                is-dir
                                starts-with-mdump
                                (= 1 child-count))]
        (if is-mbdump-archive
          (let [processed-mdump (process-mbdump f)]
            (swap! table-file-map assoc (keyword (.getName f)) processed-mdump)))))
    @table-file-map))


(defn list-files [root-dir-path]
  (let [
    ; root-f (clojure.java.io/file root-dir-path)
        ; fc (file-seq root-f)
        ; fs (filter #(.isFile %) (file-seq f))
        table-file-map (build-table-file-map root-dir-path)
        ; errors (atom {})
        db-list (sort (flatten (map #(keys %) (vals table-file-map))))
        ]
    (doseq [tbl-key db-list]
      (let [filename (find-nested table-file-map tbl-key)
            file (clojure.java.io/file filename)]
        (with-open [rdr (clojure.java.io/reader file)]
          (read-linez tbl-key (line-seq rdr)))
        ))))

;     (doseq [file  fs]
;       (let [fname (.getName file)
;             kwname (keyword fname)
;             tbfl-found? (contains? mbtab/table-defs kwname)]
;         (if tbfl-found?
;           (swap! tbfl-map assoc kwname file)
;           (do
;             ;; PROBLEM -- Table<=>File mapping not found!!!
;             (println "unable to find key for table-file: ")
;             (println "  - fname: " fname)
;             (println "  - kwname: " kwname)
;             (swap! errors assoc kwname [fname kwname])
;             ; (pp/pprint (sort (keys mbtab/table-defs)))
;             ; (pp/pprint @tbfl-map)
;
;
;
;             ))
;
; ;; now sum both found and errors and see the diff for table-defs
;       processed-keys (conj @tbfl-map (keys @errors))
;       table-keys (keys mbtab/table-defs)
;       ; (with-open [rdr (clojure.java.io/reader file)]
;       ;    (let [lines (line-seq rdr)]
;       ;      (doseq [l lines]
;       ;        (read-linez l))))
;              ))
             ; (println "Errors: ")
             ; (pp/pprint (sort @errors))
             ; (println "Completed Map: ")
             ; (pp/pprint (sort (keys @tbfl-map))


;
;       (let [lr (read-file file)]
;         (doseq [line lr]
;           (read-line line)))
; )
;
;
;       ))

    ; (loop [fl (first fs)
    ;        fss (rest fs)]
    ;   (println fl)
    ;   (println " ")
    ;   (recur (first fss)
    ;          (rest fss)))))

; (first fs)
; ;;=> #<File c:\clojure-1.2.0>
; (clojure.pprint/pprint (take 10 fs))
;   )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  ; "/Users/brian/dev_space/GitHub-data-datomic/data-io--data-sources/musicbrainz/MusicBrainz/mbdump"
  (let [import-dir "/Users/brian/dev_space/GitHub-data-datomic/data-io--data-sources/musicbrainz/MusicBrainz/08.29.2020"]
    (list-files import-dir)
    )


  )
