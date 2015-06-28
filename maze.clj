(def maze (vector ["x" "x" "x" "x" "x" "x"]
["0" "x" "0" "0" "0" "x"]
["x" "*" "0" "x" "0" "x"]
["x" "x" "x" "x" "0" "x"]
["0" "0" "0" "0" "0" "x"]
["0" "x" "x" "x" "0" "x"]))

(def paths (quote ()))
(declare walk_maze the_maze floor position steps)

(defn get_start_position
  "Checks a floor array and returns the index of the start marker(*), if it can be found and -1 otherwise."
  [floor_array]
  (.indexOf floor_array "*"))

(defn get_start_floor
  "Checks the maze matrix and gets the array which contains the start position marker(*)."
  [the_maze index]
  (cond (empty? the_maze) -1
    (= (get_start_position (first the_maze)) -1) (get_start_floor (rest the_maze) (inc index))
        :else index))

(defn found_exit?
  "Returns true if we are at the exit of the maze (false otherwise)."
  [the_maze floor position]
  ;If we have reached the most east, west, north or south side of the maze, then we are at the exit.
  (or
    (or (= 0 floor) (= floor (dec (count the_maze))))
    (or (= 0 position) (= position (dec (count (the_maze floor)))))
    ))

(defn can_go_right?
  [the_maze floor position]
  (and
    (< (inc position) (count (the_maze floor)))
    (= "0" (get (the_maze floor) (inc position)))))

(defn can_go_up?
  [the_maze floor position]
  (and
    (> (dec floor) -1)
    (= "0" (get (the_maze (dec floor)) position))))

(defn can_go_left?
  [the_maze floor position]
  (and
    (> (dec position) -1)
    (= "0" (get (the_maze floor) (dec position)))))

(defn can_go_down?
  [the_maze floor position]
  (and
    (< (inc floor) (count the_maze))
    (= "0" (get (the_maze (inc floor)) position))))

(defn go_right
  [the_maze floor position steps]
  (walk_maze
    (assoc the_maze floor (assoc (the_maze floor) position "m"))
    floor
    (inc position)
    (cons ["MOVING" floor position] steps)))

(defn go_up
  [the_maze floor position steps]
  (walk_maze
    (assoc the_maze floor (assoc (the_maze floor) position "m"))
    (dec floor)
    position
    (cons ["MOVING" floor position] steps)))

(defn go_left
  [the_maze floor position steps]
  (walk_maze
    (assoc the_maze floor (assoc (the_maze floor) position "m"))
    floor
    (dec position)
    (cons ["MOVING" floor position] steps)))

(defn go_down
  [the_maze floor position steps]
  (walk_maze
    (assoc the_maze floor (assoc (the_maze floor) position "m"))
    (inc floor)
    position
    (cons ["MOVING" floor position] steps)))

(defn walk_maze
  [the_maze floor position steps]
    (if (found_exit? the_maze floor position) (def paths (cons (cons ["EXIT" floor position] steps) paths)) )
    (if (can_go_right? the_maze floor position) (go_right the_maze floor position steps) )
    (if (can_go_up? the_maze floor position) (go_up the_maze floor position steps) )
    (if (can_go_left? the_maze floor position) (go_left the_maze floor position steps) )
    (if (can_go_down? the_maze floor position) (go_down the_maze floor position steps)) )

(def start_floor (get_start_floor maze 0))
(def start_pos (get_start_position (maze start_floor)))

(walk_maze maze start_floor start_pos '())
(def paths (sort (fn [x y] (< (count x) (count y))) paths))
(println (str "The shortest path in the maze is: " (count (first paths)) " steps long."))
(println (str "The path is " (first paths)))
