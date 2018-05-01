(ns planmill-sum.core
  (:gen-class)
  (require [clojure.data.xml :as xml]
           [dk.ative.docjure.spreadsheet :as s]))

(defn parse-row [row]
  (map #(-> % :content first :content first) row))

(defn convert-number [str]
  (let [is-string-num (re-find #"^[0-9]+\.?[0-9]*$" str)]
    (if (nil? is-string-num) str (read-string str))))

(defn set-formulas! [sheet first-row]
  (loop [row first-row]
    (let [cell (s/select-cell (str "F" row) sheet)]
      (if (nil? cell)
        nil
        (do (.setCellFormula cell (str "B" row "*E" row))
          (recur (inc row)))))))

(defn process-excel [source target]
  (let [xml-data (xml/parse-str (slurp source))
        rows (-> xml-data :content (nth 2) :content first :content)
        parsed-data (map #(-> % :content parse-row) rows)
        numeric-data (->> parsed-data
                          (map rest)
                          (map #(map convert-number %))
                          (map vec)
                          (map #(conj % "")))
        wb (s/create-workbook "Hours" numeric-data)]
    (set-formulas! (s/select-sheet "Hours" wb) 2)
    (s/save-workbook! target wb)))

(defn -main
  [source target]
  (process-excel source target))
