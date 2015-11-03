;This is the maze we need to traverse. Paths with "x" are blocked. Our start position is at "*".
(def maze
  (vector
    ["x" "x" "x" "x" "x" "x"]
    ["0" "x" "0" "0" "0" "x"]
    ["x" "*" "0" "x" "0" "x"]
    ["x" "x" "x" "x" "0" "x"]
    ["0" "0" "0" "0" "0" "x"]
    ["0" "x" "x" "x" "0" "x"]))

(declare walk-maze the-maze floor position steps)

(defn get-start-position
  "Checks a floor vector and returns the index of the start marker(*), if it can be found and -1 otherwise."
  [floor-vector]
  (.indexOf floor-vector "*"))

(defn get-start-floor
  "Checks the maze matrix and gets the vector which contains the start position marker(*)."
  [the-maze index]
  (cond (empty? the-maze) -1
        (= (get-start-position (first the-maze)) -1) (get-start-floor (rest the-maze) (inc index))
        :else index))

(defn found-exit?
  "Returns true if we are at the exit of the maze (false otherwise)."
  [the-maze floor position]
  ;If we have reached the most east, west, north or south side of the maze, then we are at the exit.
  (or
    (or (= 0 floor) (= floor (dec (count the-maze))))
    (or (= 0 position) (= position (dec (count (the-maze floor)))))))

;We can go right if our position does not exceed the boundaries of the maze AND we cannot step on an "x".
(defn can-go-right?
  [the-maze floor position]
  (and
    (< (inc position) (count (the-maze floor)))
    (= "0" (get (the-maze floor) (inc position)))))

;We can go up if our floor does not pass floor 0 (it is the highest floor) AND we cannot step on an "x".
(defn can-go-up?
  [the-maze floor position]
  (and
    (> (dec floor) -1)
    (= "0" (get (the-maze (dec floor)) position))))

;We can go left if our position does not pass position 0 (it is the leftmost position) AND we cannot step on an "x".
(defn can-go-left?
  [the-maze floor position]
  (and
    (> (dec position) -1)
    (= "0" (get (the-maze floor) (dec position)))))

;We can go down if our floor does not exceed the boundaries of the maze AND we cannot step on an "x".
(defn can-go-down?
  [the-maze floor position]
  (and
    (< (inc floor) (count the-maze))
    (= "0" (get (the-maze (inc floor)) position))))

;We go right by increasing our position by 1. Also, mark where we stepped with "m" (prevents going back and forth endlessly).
(defn go-right
  [the-maze floor position steps]
  (cond (can-go-right? the-maze floor position)
        (walk-maze
          (assoc the-maze floor (assoc (the-maze floor) position "m"))
          floor
          (inc position)
          (cons [floor position] steps))
        :else '()))

;We go up by decreasing our floor by 1 (the highest floor is 0).
;Also, mark where we stepped with "m" (prevents going back and forth endlessly).
(defn go-up
  [the-maze floor position steps]
  (cond (can-go-up? the-maze floor position)
        (walk-maze
          (assoc the-maze floor (assoc (the-maze floor) position "m"))
          (dec floor)
          position
          (cons [floor position] steps))
        :else '()))

;We go left by decreasing our position by 1. Also, mark where we stepped with "m" (prevents going back and forth endlessly).
(defn go-left
  [the-maze floor position steps]
  (cond (can-go-left? the-maze floor position)
        (walk-maze
          (assoc the-maze floor (assoc (the-maze floor) position "m"))
          floor
          (dec position)
          (cons [floor position] steps))
        :else '()))

;We go down by increasing our floor by 1. Also, mark where we stepped with "m" (prevents going back and forth endlessly).
(defn go-down
  [the-maze floor position steps]
  (cond (can-go-down? the-maze floor position)
        (walk-maze
          (assoc the-maze floor (assoc (the-maze floor) position "m"))
          (inc floor)
          position
          (cons [floor position] steps))
        :else '()))

;Traverses the maze recursively and returns all possible paths as lists of steps.
;The steps are saved as vectors, each having 2 elements: the first is the current row, and second is the current column.
(defn walk-maze
  [the-maze floor position steps]
  (cond
    (found-exit? the-maze floor position) (list (cons [floor position] steps)) 
    :else (concat
            (go-right the-maze floor position steps)
            (go-up the-maze floor position steps)
            (go-left the-maze floor position steps)
            (go-down the-maze floor position steps))))

(def start-floor (get-start-floor maze 0))
(def start-pos (get-start-position (maze start-floor)))
(def paths (walk-maze maze start-floor start-pos '()))
;Sorting the paths by number of steps:
(def sorted-paths (sort (fn [x y] (< (count x) (count y))) paths))

(println (str "The shortest path in the maze is: " (count (first sorted-paths)) " steps long."))
(println (str "The path is " (reverse (first sorted-paths))))
