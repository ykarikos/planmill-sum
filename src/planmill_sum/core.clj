(ns planmill-sum.core
  (:gen-class)
  (require [clojure.data.xml :as xml]
           [dk.ative.docjure.spreadsheet :as s]))

(def monetary-style-string "# ##0.00\" €\"")

(defn parse-row [row]
  (map #(-> % :content first :content first) row))

(defn convert-number [str]
  (let [is-string-num (re-find #"^[0-9]+\.?[0-9]*$" str)]
    (if (nil? is-string-num) str (read-string str))))

(defn convert-date [row]
  (let [datetime (first row)
        date (re-find #"^[0-9-]+" datetime)]
    (cons
      (if (nil? date)
        datetime
        date)
      (rest row))))

(defn set-formulas! [sheet first-row]
  (loop [row first-row]
    (let [cell (s/select-cell (str "F" row) sheet)]
      (if (nil? cell)
        row
        (do
          (.setCellFormula cell (str "B" row "*E" row))
          (recur (inc row)))))))

(defn set-styles! [sheet style columns row-start row-end]
  (doseq [row (range row-start row-end)
          column columns]
    (let [cell (s/select-cell (str column row) sheet)]
      (s/set-cell-style! cell style))))

(defn process-excel [source target]
  (let [xml-data (xml/parse-str (slurp source))
        rows (-> xml-data :content (nth 2) :content first :content)
        parsed-data (map #(-> % :content parse-row) rows)
        numeric-data (->> parsed-data
                          (map rest)
                          (map #(map convert-number %))
                          (map convert-date)
                          (map vec)
                          (map #(conj % "Sum")))
        wb (s/create-workbook "Hours" numeric-data)
        monetary-style (s/create-cell-style! wb {:data-format monetary-style-string})
        sheet (s/select-sheet "Hours" wb)
        row-count (set-formulas! sheet 2)]
    (set-styles! sheet monetary-style ["B" "F"] 2 row-count)
    (s/save-workbook! target wb)))

(defn -main
  [source target]
  (process-excel source target))
