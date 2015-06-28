#This is the maze we need to traverse. Paths with "x" are blocked. Our start position is at "*".
maze = [
  ["x", "x", "x", "x", "x", "x"],
  ["0", "x", "0", "0", "0", "x"],
  ["x", "*", "0", "x", "0", "x"],
  ["x", "x", "x", "x", "0", "x"],
  ["0", "0", "0", "0", "0", "x"],
  ["0", "x", "x", "x", "0", "x"]
] 

$paths = []

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

#We need to copy the maze, otherwise some paths could cross other paths and close them with "m".
#We also need to copy the steps array, this way each path will be contained in it's own array.
#Note that we copy steps with the + operator, which returns a new array by adding two arrays.
def copy_object(object) 
  Marshal.load(Marshal.dump(object))
end

#Traverses the maze recursively and saves all it's paths in a FIFO queue called $paths.
#The steps are saved as arrays, each having 3 elements.
#The first element indicates if this is the exit of the maze.
#The second is the current row, and the third is the current column.
def walk_maze(the_maze, floor, position, steps=[]) 
  if found_exit(the_maze, floor, position)
    #When we have found an exit, we add the steps array in $paths.
    $paths.unshift(steps + [["EXIT", floor, position]])
  end
  #Mark path and Go RIGHT. 
  if (position + 1 < the_maze[floor].size) and (the_maze[floor][position + 1] == "0") 
    the_maze[floor][position] = "m" #NOTE: We mark the path we traveled so that we don't go back and forth forever.
    walk_maze(copy_object(the_maze), floor, position + 1, steps + [["MOVING", floor, position]])
  end
  #Mark path and Go UP. 
  if (floor - 1 > -1) and (the_maze[floor - 1][position] == "0") 
    the_maze[floor][position] = "m" 
    walk_maze(copy_object(the_maze), floor - 1, position, steps + [["MOVING", floor, position]])
  end
  #Mark path and Go LEFT. 
  if (position - 1 > -1) and (the_maze[floor][position - 1] == "0") 
    the_maze[floor][position] = "m" 
    walk_maze(copy_object(the_maze), floor, position - 1, steps + [["MOVING", floor, position]])
  end
  #Mark path and Go DOWN. 
  if (floor + 1 < the_maze.size) and (the_maze[floor + 1][position] == "0") 
    the_maze[floor][position] = "m" 
    walk_maze(copy_object(the_maze), floor + 1, position, steps + [["MOVING", floor, position]])
  end
end 

#Get our start position.
start_floor = get_start_floor(maze) 
start_position = get_start_position(maze[start_floor]) 

#Walk the maze from the start position and get the shortest path.
walk_maze(maze, start_floor, start_position)
shortest_path = $paths.sort{|x, y| x.size <=> y.size}.first

puts "The shortest path in the maze is #{shortest_path.size} steps long. \nHere is the path: \n#{shortest_path}"
