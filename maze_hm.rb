require "hamster"

#This is the maze we need to traverse. Paths with "x" are blocked. Our start position is at "*".
maze = Hamster.vector(
  Hamster.vector("x", "x", "x", "x", "x", "x"),
  Hamster.vector("0", "x", "0", "0", "0", "x"),
  Hamster.vector("x", "*", "0", "x", "0", "x"),
  Hamster.vector("x", "x", "x", "x", "0", "x"),
  Hamster.vector("0", "0", "0", "0", "0", "x"),
  Hamster.vector("0", "x", "x", "x", "0", "x")
) 

#Checks a floor array and returns the index of the start marker, if it can be found and nil otherwise. 
def get_start_position(floor_array) 
  floor_array.index("*") 
end 

#Checks the maze matrix and gets the array which contains the start position marker. 
def get_start_floor(the_maze) 
  the_maze.each_with_index do |floor, index| 
    return index if get_start_position(floor) != nil 
  end 
end

#Returns true if we are at the exit of the maze (false otherwise).
def found_exit(the_maze, floor, position) 
  #If we have reached the most east, west, north or south side of the maze, then we are at the exit.
  (floor == 0 or floor == the_maze.size - 1) or (position == 0 or position == the_maze[floor].size - 1)
end

def can_go_right?(the_maze, floor, position)
  (position + 1 < the_maze[floor].size) and (the_maze[floor][position + 1] == "0")
end

def can_go_up?(the_maze, floor, position)
  (floor - 1 > -1) and (the_maze[floor - 1][position] == "0") 
end

def can_go_left?(the_maze, floor, position)
  (position - 1 > -1) and (the_maze[floor][position - 1] == "0") 
end

def can_go_down?(the_maze, floor, position)
  (floor + 1 < the_maze.size) and (the_maze[floor + 1][position] == "0") 
end

#Traverses the maze recursively and returns all possible paths as arrays of steps.
#The steps are saved as arrays, each having 2 elements: the first is the current row, and second is the current column.
#Note that the last two arguments (steps and paths) should be left at default when the user calls the function.
def walk_maze(the_maze, floor, position, steps=Hamster.vector, paths=[]) 
  if found_exit(the_maze, floor, position)
    #When we have found an exit, we add the steps array in paths.
    paths.unshift(steps.push([floor, position]))
  end
  if can_go_right?(the_maze, floor, position)
    the_maze = the_maze.set(floor, the_maze[floor].set(position, "m"))
    walk_maze(the_maze, floor, position + 1, steps.push([floor, position]), paths)
  end
  if can_go_up?(the_maze, floor, position)
    the_maze = the_maze.set(floor, the_maze[floor].set(position, "m"))
    walk_maze(the_maze, floor - 1, position, steps.push([floor, position]), paths)
  end
  if can_go_left?(the_maze, floor, position)
    the_maze = the_maze.set(floor, the_maze[floor].set(position, "m"))
    walk_maze(the_maze, floor, position - 1, steps.push([floor, position]), paths)
  end
  if can_go_down?(the_maze, floor, position)
    the_maze = the_maze.set(floor, the_maze[floor].set(position, "m"))
    walk_maze(the_maze, floor + 1, position, steps.push([floor, position]), paths)
  end

  return paths
end 

#Get our start position.
start_floor = get_start_floor(maze) 
start_position = get_start_position(maze[start_floor]) 

#Walk the maze from the start position. This should enumerate all paths in $paths.
paths = walk_maze(maze, start_floor, start_position)

#Find the shortest path.
shortest_path = paths.sort{|x, y| x.size <=> y.size}.first

puts "The shortest path in the maze is #{shortest_path.size} steps long. \nHere is the path: \n#{shortest_path.inspect}"
