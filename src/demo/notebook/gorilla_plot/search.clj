
;;; # Search
;;; 
;;; Lecture notes on AI search algorithms, with Clojure code.
;;; 
;;; Lee Spector, lspector@hampshire.edu, 20141015-20170930
;;; 
;;; Some of the code here was adapted from the [search.lisp](https://norvig.com/paip/search.lisp) Common Lisp code in Peter Norvig's Paradigms of AI Programming (1991), but with substantial modifications.



(ns  demo.notebook.gorilla-plot.search)

;;; In AI, the concept of "search" is applied broadly. 
;;; Every problem-solving process can be considered to be a search through an abstract "space" of potential answers. 
;;; For example, we can think of the process of solving a Rubik's cube as  navigating an abstract space in which each location is associated with one configuration of the cube, and each neighboring location is associated with a configuration that's one twist of the cube away. The problem-solver aims to find a path through this space that reaches a location associated with a configuration in which each face is uniformly colored.
;;; The process of proving a theorem in symbolic logic can be thought of as searching through a space in which each location is associated with a set of logical statements, and in which one is a neighbor of another if its statements can be produced by applying a rule of inference to the statements of the other.
;;; The process of planning a day's activites can be thought of as searching through the space of all possible arrangements of tasks, trying to find one that meets all requirements and also accomplishes as many optional goals as possible.
;;; And so on.
;;; In some cases the "search space" may be complex, with many branches and alternative paths from one place to another. 
;;; In some, it can be "deceptive," in the sense that you can only reach a good place by going through worse places. 
;;; In some, different agents control moves between different locations. For example, the search space for chess can be thought of as having a location for every possible configuration of pieces, with locations being linked if one can be reached from the other by one legal move. A player's goal is to find a path through the space that leads to a configuration in which it wins, but the player only gets to make half of the moves through the space. So it should make a move from which _all_ of the counter-moves that its opponent _could_ make lead to places that will allow it to reach a winning configuration.
;;; 
;;; Once we're thinkling of all problem-solving processes as kinds of search, we can consider different kinds of search algorithms, and which may be appropriate for which kinds of problems. Some can be solved efficiently with deterministic, exhaustive search algorithms, while others involve uncertainty or require guesses.
;;; 
;;; Most interesting applications of AI search will involve huge spaces that we don't actually have in our programs explicitly. For example, a chess program won't have all possible configurations of pieces in memory at the same time. Rather, it'll have one or a small subset of them in memory, and generate others as it considers moves.
;;; 
;;; Nonetheless, we can begin to explore search algorithms by considering how we could search for something in a small, finite space, even one as simple as a flat list.

(def space [2 4 2 39 1 2 3 5 7 298 1 2 39 5 4 3])

(defn satisfies-goal?
  [n]
  (= n 5))

;;; Now one way to do the search is just to use Clojure's built-in `filter` function.

(filter satisfies-goal? space)

;;; If we only need to find one thing that satisfies the goal, we can wrap that in `first`.

(first (filter satisfies-goal? space))

;;; By the way, `filter` returns a lazy sequence, computing only as much as it needs. So if we only ask for the `first` element of its result, then it won't necessarily process the whole sequence. It'll just go as far as it needs to go to get the first item that satisfies the goal.
;;; To make it even neater, we can package `filter` and `first` into a single `search` function.	

(defn search
  [goal-function flat-sequence]
  (first (filter goal-function flat-sequence)))

(search satisfies-goal? space)

;;; We can pass in other goals, and other spaces.

(search odd? [2 4 6 8 10 11 29 38 1 7])

;;; For the goals we can use anonymous functions if that's more convenient.

(search (fn [n] (> n 20))
        [2 4 6 8 10 11 29 38 1 7])

;;; The abbreviated anonymous function syntax can be used too.

(search #(> % 20)
        [2 4 6 8 10 11 29 38 1 7])

;;; The `filter` function is great, because it's built in and does the searching for you. But we're going to want to search spaces that aren't just linear sequences, and we're going to want to explore different ways of exploring the space. So we're going to have to build our own search functions.
;;; 
;;; Let's start with a function that still just searches a linear sequence, but uses an explicit `loop` rather than `filter` to look for something that satisfies the goal. We'll write it to check for success with `satisfies-goal?`, which we defined above to return true only for an input of 5.

(defn search
  [sequence]
  (loop [remaining sequence]
    (if (empty? remaining)
      false
      (if (satisfies-goal? (first remaining))
        (first remaining)
        (recur (rest remaining))))))

(search [1 2 3 4 5 6 7 8 9 10])

(search [1 2 3])

;;; Now let's re-write it to take the goal function as another parameter.

(defn search
  [goal sequence] ;;***
  (loop [remaining sequence]
    (if (empty? remaining)
      false
      (if (goal (first remaining)) ;;***
        (first remaining)
        (recur (rest remaining))))))

(search even? [1 2 3 4 5 6 7 8 9 10])

;;; Now let's begin to generalize it and make it smarter.
;;; In a search space that's a linear sequence, there's just one "next" item to look at after each item that you examine. There's never a choice about which thing to look at next. 
;;; 
;;; In contrast, in most interesting search spaces that we'll want to explore, there will be several possible next items. We think of the overall search space in such cases as a "tree" composed of branches that lead to "leaves" (the actual items) and to subtrees, which may themselves be composed of branches to leaves or smaller subtrees, etc.
;;; 
;;; For historical reasons, computer scientists generally draw trees upside down, with branches going down. And when we move from the main tree into a subtree, and then into a subtree of the subtree, etc., we say that we are going "deeper" into the tree. And we call the starting point of the search the "root." And we call each item in the tree a "node," or sometimes a "state." Weird, but that's the standard terminology. 
;;; 
;;; In Clojure, if we want to explicitly represent an entire search tree then we can do so in a nested vector like `[1 [3 5 7 8] [[[4 5] 6] 7]]`. There are three main branches here, one of which goes directly to a leaf (`1`), one of which goes to a subtree with 4 leaves, and one of which goes to a subtree composed of smaller subtrees.
;;; 
;;; When you have a tree-structured search space, you have choices about what order in which to search it. The two most basic search strategies are "depth-first" and "breadth first" search.
;;; 
;;; In depth-first search, we follow the first branch, and then the first branch of the first branch, and so on, deeper and deeper until we hit a leaf. Then we check to see if the leaf satisfies the goal. If it does, then we're done. If it doesn't, then we back up to our last place where there were multiple options, and explore the next option, again going all the way to a leaf before checking anything. Until we find a solution or exhaust the entire tree, we keep following this strategy, of going deep first, and when the test for whether a leaf satisfies the goal fails, backing up to the last choice point that hasn't yet been fully explored.
;;; 
;;; In breadth-first search, by contrast, we first check all of the leaves that are one step away from the root. If none of them satisfy the goal, then we check all of the leaves that are two steps away from the goal, and so on.
;;; 
;;; Here is a version of the `search` function with the explicit loop, above, that has been changed to search a tree rather than a linear sequence. The lines that have been changed are marked with `;;***`.

(defn depth-search ;;***
  [goal tree]      ;;***
  (loop [remaining tree]
    (if (empty? remaining)
      false
      (if (sequential? (first remaining)) ;;***
        (recur (concat (first remaining) (rest remaining))) ;;***
        (if (goal (first remaining))
          (first remaining)
          (recur (rest remaining)))))))

(depth-search even? [1 [3 5 7 8] [[[4 5] 6] 7]])

;;; We can use `let` to make `f` and `r` shorthand for `(first remaining)` and `(rest remaining)`, which will make some of our subsequent steps neater.

(defn depth-search
  [goal tree]
  (loop [remaining tree]
    (if (empty? remaining)
      false
      (let [f (first remaining) ;;***
            r (rest remaining)] ;;***
        (if (sequential? f)
          (recur (concat f r))
          (if (goal f)
            f
            (recur r)))))))

(depth-search even? [1 [3 5 7 8] [[[4 5] 6] 7]])

;;; It can be instructive to print each item as you check it.

(defn depth-search
  [goal tree]
  (loop [remaining tree]
    (if (empty? remaining)
      false
      (let [f (first remaining)
            r (rest remaining)]
        (println "checking:" f) ;; ***
        (if (sequential? f)
          (recur (concat f r))
          (if (goal f)
            f
            (recur r)))))))

(depth-search even? [1 [3 5 7 8] [[[4 5] 6] 7]])

;;; Now let's define `breadth-search` similarly, but with `f` and `r` combined in the opposite way in `remaining` (which is the remaining space to search) when we return to the top of the loop.

(defn breadth-search ;;***
  [goal tree]
  (loop [remaining tree]
    (if (empty? remaining)
      false
      (let [f (first remaining)
            r (rest remaining)]
        (println "checking:" f)
        (if (sequential? f)
          (recur (concat r f)) ;;***
          (if (goal f)
            f
            (recur r)))))))

(breadth-search even? [1 [3 5 7 8] [[[4 5] 6] 7]])

(breadth-search even? [1 [3 5 7 8] [[[4 5] 6] 7] 10])

;;; We can write a more general `search` function that takes an argument that indicates whether to perform depth-first or breadth-first search. In fact, since these differ just in the way toat `f` and `r` are combined, we can write `search` to take a `combiner` function, so that we can call it with one function to perform depth-first search, another to perform breadth-first search, and yet others to perform other kinds of search.

(defn search
  [goal tree combiner] ;;***
  (loop [remaining tree]
    (if (empty? remaining)
      false
      (let [f (first remaining)
            r (rest remaining)]
        (println "checking:" f)
        (if (sequential? f)
          (recur (combiner f r)) ;;***
          (if (goal f)
            f
            (recur r)))))))

;;; We can now define `depth-search` as a call to `search` with a combiner of `concat`.

(defn depth-search
  [goal tree]
  (search goal tree concat))

(depth-search even? [1 [3 5 7 8] [[[4 5] 6] 7] 10])

;;; We can define `breadth-search` in a similar way, but providing a function that `concat`s its arguments in backwards order as the `combiner`.

(defn breadth-search
  [goal tree]
  (search goal tree #(concat %2 %1)))

(breadth-search even? [1 [3 5 7 8] [[[4 5] 6] 7] 10])

;;; For some search problems, we'll want to keep track of how we got to each branch of the tree.
;;; 
;;; We can do this by replacing each item in the tree, as we explore it, with a map with values for the keys `:contents` and `:history`. The value for `:contents` will be the actual item at that point in the search space, while the value for `:history` will be the sequence of items that were explored on the way to this point.
;;; We can replace the items in the tree with these maps one level at a time, as we search the tree.

(defn search
  [goal tree combiner]
  (loop [remaining (map #(hash-map :contents % :history []) tree)] ;;***
    (if (empty? remaining)
      false
      (let [f (first remaining)
            r (rest remaining)]
        (if (sequential? (:contents f))
          (recur
           (combiner
            (map #(hash-map :contents %
                            :history (conj (:history f) (:contents f))) ;;***
                 (:contents f))
            r))
          (if (goal (:contents f))
            f
            (recur r)))))))

(defn depth-search
  [goal tree]
  (search goal tree concat))

(depth-search even? [1 [3 5 7 8] [[[4 5] 6] 7] 10])

(depth-search even? [1 [3 5 7] [[[4 5] 6] 7] 10])

(defn breadth-search
  [goal tree]
  (search goal tree #(concat %2 %1)))

(breadth-search even? [1 [3 5 7 8] [[[4 5] 6] 7] 10])

(breadth-search even? [1 [3 5 7] [[[4 5] 6] 7]])

;;; Now we'll take a big step, of switching from searching an explicitly specified space to searching a space that we create as we explore it. This is what we'll usually want to do in real applications of AI, because the search spaces are usually much too big to generate completely. 
;;; 
;;; We'll usually start at one place in the space (like an initial configuration of a Rubik's cube) and, if the solution isn't at that location, generate the "successors" at the neighboring locations in the space.
;;; 
;;; We can generalize our `search` function to work in this context by writing it to take another argument, a function that takes a node of the tree and returns all of its successors. We'll also print the "frontier" of the search tree -- all of the states that have been generated but not yet explored -- as we proceed.

(defn search
  [goal start combiner successors] ;;***
  (loop [frontier [(hash-map :contents start :history [])]]
    (if (empty? frontier)
      false
      (let [f (first frontier)
            r (rest frontier)]
        (println "Frontier:" (map :contents frontier) "Checking:" (:contents f))
        (if (goal (:contents f))
          f
          (recur
           (combiner
            (map #(hash-map :contents %
                            :history (conj (:history f) (:contents f)))
                 (successors (:contents f)))
            r)))))))

;;; For most real problems there will be a natural successor function, for example when solving a Rubik's cube the successors will be all of the configurations reachable by one twist from the current configuration.
;;; 
;;; But to demonstrate this function first with searches through numbers, let's use the concept of a binary tree to generate a tree-structured space of the natural numbers.
;;; 
;;; The binary tree we'll use looks like this:
;;; 
;;; ```
;;;         1
;;;      2     3 
;;;     4 5   6 7
;;; ```
;;; and so on. It starts at 1, which has two successors, 2 and 3. 2 has two successors, 4 and 5. And so on.
;;; 
;;; This means that the successors of any number are that number doubled, and that number doubled plus one.

(defn binary-tree [x]
  [(* 2 x) (inc (* 2 x))])

(binary-tree 23)

;;; Now we can use this function to perform a depth-first search of a binary tree, looking for a number greater than 20.

(search #(> % 20) 1 concat binary-tree)

;;; And we can also do this with breadth-first search.

(search #(> % 20) 1 #(concat %2 %1) binary-tree)

;;; Searching for numbers in a binary tree is an artificial and pretty useless thing to do, but the `search` function we've developed here can be used for _any_ problem for which we can specify a `goal` function, a `start` state, and a `successors` function. And by providing different `combiner` functions, we can get it to search the space in different orders.
;;; 
;;; Let's illustrate this by considering the sliding tile puzzle called the "8 puzzle" (which is a smaller version of the [15 puzzle](https://en.wikipedia.org/wiki/15_puzzle)).
;;; 
;;; We'll represent a state in the search space as a vector of the numbers from 0 to 8, where the 0 is the empty space into which adjacent tiles can be slid. 
;;; 
;;; As an example, the vector [8 2 3 1 0 4 5 6 7] corresponds to this puzzle board:
;;; 
;;; ```
;;; 8 2 3
;;; 1   4
;;; 5 6 7
;;; ```
;;; The empty space is in the middle. From this position, sliding one tile, you could get:
;;; ```
;;; 8   3
;;; 1 2 4
;;; 5 6 7
;;; ```
;;; or
;;; ```
;;; 8 2 3
;;;   1 4
;;; 5 6 7
;;; ```
;;; or
;;; ```
;;; 8 2 3
;;; 1 4
;;; 5 6 7
;;; ```
;;; or 
;;; ```
;;; 8 2 3
;;; 1 6 4
;;; 5   7
;;; ```
;;; The goal of the puzzle is to start at some other state and to reach [1 2 3 4 5 6 7 8 0], which looks like this:
;;; ```
;;; 1 2 3
;;; 4 5 6
;;; 7 8
;;; ```
;;; 
;;; We'll write a function that takes a puzzle board and two positions, and returns the boards with the contents of the positions swapped. We'll use this later to swap the 0 with the value in an adjacent position.
;; **

(defn slide
  [from-index to-index board]
  (-> board
      (assoc from-index (get board to-index))
      (assoc to-index (get board from-index))))

;;; Now we can write a function that returns the successors of any board -- that is, the boards reachable by sliding one tile.

(defn eight-puzzle-moves
  "Returns a vector of all of the eight-puzzle boards reachable by sliding a 
  tile into the empty (zero) space in board b. Note that the indices of positions
  in boards are as follows:
  [0 1 2
   3 4 5
   6 7 8]"
  [b]
  (case (count (take-while pos? b)) ;; this gives us the index of the 0
    0 [(slide 1 0 b) (slide 3 0 b)]
    1 [(slide 0 1 b) (slide 2 1 b) (slide 4 1 b)]
    2 [(slide 1 2 b) (slide 5 2 b)]
    3 [(slide 0 3 b) (slide 4 3 b) (slide 6 3 b)]
    4 [(slide 1 4 b) (slide 3 4 b) (slide 5 4 b) (slide 7 4 b)]
    5 [(slide 2 5 b) (slide 4 5 b) (slide 8 5 b)]
    6 [(slide 3 6 b) (slide 7 6 b)]
    7 [(slide 4 7 b) (slide 6 7 b) (slide 8 7 b)]
    8 [(slide 5 8 b) (slide 7 8 b)]))

;;; Let's also define a function that takes a board and tells us whether it's a solution.

(defn eight-puzzle-solution
  [board]
  (= board [1 2 3 4 5 6 7 8 0]))

;;; Now we're ready to solve the puzzle with tree search!
;;; 
;;; Here's how we solve it from this initial state:
;;; ```
;;; 1 2 3
;;; 4 5 6
;;;   7 8
;;; ```
;;; This is obviously quite easy, since we just have to slide the 7 to the left, and then the 8. We'll do it here with breadth-first search.
;; **

(search eight-puzzle-solution
        [1 2 3 4 5 6 0 7 8]
        #(concat %2 %1)
        eight-puzzle-moves)

;;; Many other starting points take too long. We'll make several improvements before trying them.
;;; 
;;; First, it's important to realize that the tree we'll be producing here will include many duplicate states. Every time we slide a tile, one of the successors of the resulting state will be the original state before the slide, since a legal next move will be to just slide the tile back. There are other ways to get back to the same states as well, and the number of duplicates will quickly get out of hand.
;;; 
;;; Here we'll re-define `search` to keep track of all of the states that have been seen, in a set, and remove any already-seen states from successors before we explore them. We'll also stop printing the frontier, since it'll be annoyingly verbose for any non-trivial search.

(defn search
  [goal start combiner successors]
  (loop [frontier [(hash-map :contents start :history [])]
         seen #{start} ;;***
         steps 0]      ;;***
    (if (empty? frontier)
      false
      (let [f (first frontier)
            r (rest frontier)]
        (if (goal (:contents f))
          [f {:seen (count seen) :steps steps}]
          (let [unseen-successors (clojure.set/difference ;;***
                                   (set (successors (:contents f)))
                                   seen)]
            (recur
             (combiner
              (map #(hash-map :contents %
                              :history (conj (:history f) (:contents f)))
                   unseen-successors)                   ;;***
              r)
             (clojure.set/union seen unseen-successors)  ;;***
             (inc steps))))))))                          ;;***

(search eight-puzzle-solution
        [1 2 3 4 5 6 7 0 8]
        #(concat %2 %1)
        eight-puzzle-moves)

(search eight-puzzle-solution
        [1 2 3 4 0 5 6 7 8]
        #(concat %2 %1)
        eight-puzzle-moves)

;;; Many, however, are still too hard to be solved with breadth-first search.
;;; 
;;; When neither depth-first nor breadth-first search will suffice, we can use a "heuristic" (guess!) combiner function to try to put the most promising states earlier in the list of states to explore.
;;; 
;;; A heuristic function that works pretty well for the 8 puzzle is based on the distance of each tile from where it's supposed to be, moving only left, right, up and down (not diagonally). We add up all of the distances for all of the tiles, and consider boards with the lowest total distances first. This is called the "Manhattan distance" heuristic, because it's reminiscent of the ways to calculate travel distance in a city with a rectangular grid of streets.
;;; 
;;; First we define a few utilites that will help. 

(defn xcoord
  "The x coordinate of an index into
  [0 1 2
   3 4 5
   6 7 8]"
  [index]
  (case index
    (0 3 6) 0
    (1 4 7) 1
    (2 5 8) 2))

(defn ycoord
  "The y coordinate of an index into
  [0 1 2
   3 4 5
   6 7 8]"
  [index]
  (case index
    (0 1 2) 0
    (3 4 5) 1
    (6 7 8) 2))

(defn index-distance
  "The distance between positions with the two given indices"
  [index1 index2]
  (+ (Math/abs (- (xcoord index1) (xcoord index2)))
     (Math/abs (- (ycoord index1) (ycoord index2)))))

(defn index-in-board
  "The index of the given tile in the given board"
  [tile board]
  (count (take-while #(not (= % tile)) board)))

;;; Let's try that out.

(index-in-board 3 [8 7 6 5 4 3 2 1 0])

;;; That says that the 3 tile is in position 5, which is correct (remember that the indices start at 0).
;;; 
;;; Now the Manhattan distance between two boards is just the sum of the distances of each tile from one board to the other.

(defn manhattan-distance
  [board1 board2]
  (reduce + (for [tile (range 9)]
              (index-distance (index-in-board tile board1)
                              (index-in-board tile board2)))))

(manhattan-distance [0 1 2 3 4 5 6 7 8]
                    [8 1 2 3 4 5 6 7 0])

(manhattan-distance [0 1 2 3 4 5 6 7 8]
                    [1 2 3 4 5 6 7 8 0])

;;; This makes it easy to define a function saying how far any board is from a solution.

(defn solution-distance
  [board]
  (manhattan-distance board
                      [1 2 3 4 5 6 7 8 0]))

;;; Now let's define a combiner function that uses solution distance. 
;;; 
;;; Actually, we'll use the length of a state's history (that is, how many moves it took to get to the state from the initial board) **plus** it's solution distance, which is an estimate of the total number of moves that it will take to get from the initial board to a solution. Using this as a heuristic is the core idea of the "A-star" (A\*) search algorithm.
;;; 
;;; We'll also limit the "width" of the frontier to 10, keeping only the 10 best-looking states at each step of the search. This kind of limitation is sometimes said to define a "beam" search.

(defn best-10-combiner
  [new-nodes old-nodes]
  (take 10 (sort #(< (+ (count (:history %1))
                        (solution-distance (:contents %1)))
                     (+ (count (:history %2))
                        (solution-distance (:contents %2))))
                 (concat new-nodes old-nodes))))

;;; Now we can use heuristic search for the same problem that we solved above with breadth-first search.

(search eight-puzzle-solution
        [1 2 3 4 0 5 6 7 8]
        best-10-combiner
        eight-puzzle-moves)

;;; We can see that this finds a solution more efficiently, although in this case it finds a longer solution.
;;; 
;;; This can succeed in cases that would fail with breadth-first search.

(search eight-puzzle-solution
        [0 1 2 3 4 5 6 7 8]
        best-10-combiner
        eight-puzzle-moves)

;;; If we want to ensure that we find an optimal solution (shortest number of moves), then we can do so by eliminating the beam width of 10, and if we always use a heuristic function that is "admissible," which means it that will never overestimate the true distance. The Manhattan distance heuristic can be proven to be admissible.

(defn best-combiner
  [new-nodes old-nodes]
  (sort #(< (+ (count (:history %1))
               (solution-distance (:contents %1)))
            (+ (count (:history %2))
               (solution-distance (:contents %2))))
        (concat new-nodes old-nodes)))

(search eight-puzzle-solution
        [1 2 3 4 0 5 6 7 8]
        best-combiner
        eight-puzzle-moves)

;;; An additional efficiency enhancement is to keep track of computed solution distances, so that they never have to be recomputed after they are computed the first time. This is easy to accomplish in Clojure using "memoization."

(def solution-distance (memoize solution-distance))

(search eight-puzzle-solution
        [1 2 3 4 0 5 6 7 8]
        best-combiner
        eight-puzzle-moves)

;;; Now we can solve much harder boards.

(search eight-puzzle-solution
        [0 1 2 3 4 5 6 7 8]
        best-combiner
        eight-puzzle-moves)

;;; One other source of inefficiency here is that we sort the whole frontier at each step, when all we really need to do is to move the single best state to the front of the sequence. This would run a little faster if we didn't do the unnecessary sortng, but the gains from that kind of optimization  probably won't be as significant as the gains that we get from improving the search strategy.
;;; 
;;; If you want to experiment with this search code in Gorilla REPL, then you may run into one of its weaknesses as a development environment: There's no good way to interrupt a long-running process. If you want to kill a long-running process, you pretty much have to kill and restart your gorilla process, and then reload and re-evaluate your worksheet.
;;; 
;;; So if you're working in Gorilla REPL but find yourself wanting to start/interrupt long searches, then you may want to run your code from the command line. One way to do this is to create a project with `lein new app mysearch`. Then put your code beneath the namespace declaration in `mysearch/src/mysearch/core.clj`, and your top-level call, which performs your search, in the definition of `-main`. Once you've done this,  you can still type `lein gorilla` at the command line and use Gorilla REPL to edit your code. But at any point you can also run it, not in the browser, but from a second command line. At the second command line, just navigate into the `mysearch` directory and type `lein run`. You can interrupt a search with `control-c`, but the Gorilla REPL process will not be disturbed.

