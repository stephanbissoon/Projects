# Name: Stephan Bissoon

def crossword(L):
  board = [[" "] * 20 for i in range(20)] #create 20x20 board with blanks
  last_word = 0
  switcher = 1
  valid_word = False
  reason = ""
  output = "Name: Rajiv Stephan Bissoon\nStudent ID: 500954799\n\n"
  printfile = None
  
  for a in range(len(L)): #check which word can be properly laid in the middle of board, if the word can't fit, skip it
    if valid_word:
      break

    else:
      valid_word, board = addFirstWord(L[a], board) #attempt to add the first word
      last_word = a #keep track of the index of the last attempted word

  if not valid_word:
    output += "No words can fit horizontally on the board"

  else:
    for b in range(last_word + 1, len(L)):
      if switcher % 2 == 0: #switch between vertical and horizontal
        output += "Attempting to add '" + L[b] + "' horizontally...\n"
        valid_word, board, reason = addHorizontalWord(L[b], board)

        if not valid_word: # if word cannot be added try the next word in the same direction
          output += "'" + L[b] + "' cannot be added to the board because: " + reason + "\n\n"
          continue
        
        else:
          output += "'" + L[b] + "' added successfully...\n\n"

        switcher += 1
      else:
        output += "Attempting to add '" + L[b] + "' vertically...\n"
        valid_word, board, reason = addVerticalWord(L[b], board)

        if not valid_word: # if word cannot be added try the next word in the same direction
          output += "'" + L[b] + "' cannot be added to the board because: " + reason + "\n\n"
          continue

        else:
          output += "'" + L[b] + "' added successfully...\n\n"

        switcher += 1
  
  output += "\n"

  printfile = open("output.txt", "a") #print to file
  printfile.write(output)
  printfile.close()

  printboard(board)
  print("Please check output.txt for the results")

def addFirstWord(word, board):
  center_row = 0
  start_point = 0
  
  if len(word) > len(board[0]):
    return (False, board)
    
  else:
    center_row = len(board)//2
    start_point = (len(board[0]) - len(word))//2
    
  for a in range(len(board)): #iterate array
    for b in range(len(board[a])): #iterate row
      if a == center_row and b >= start_point and (b - start_point) < len(word):
        board[a][b] = word[b - start_point]

  return (True, board)

def addHorizontalWord(word, board):
  row, column = -1, -1
  reasons = []

  for a in range(len(board)):
    for b in range(len(board[a])):
      reason = ""

      if board[a][b] == word[0]: # word will be placed to the right
        row, column = a, b
        
        if (column - 1) >= 0:
          if board[row][column - 1] != " ": #check if there is a letter behind the first letter of the word to be placed
            reasons.append("illegal adjacencies")
            reason = reasons[-1]

        for x in range(column + 1, column + len(word) + 1): #check current row to ensure there are no letters in the path and ensure there is a space after that path
          
          if x > 20: # '>' used and not '>=' to take the space into consideration should a word be placed on board limits
            reasons.append("reaches outside grid")
            reason = reasons[-1]
            break

          elif x < 20: # board can only be less than 20 indexes
            if board[row][x] != " ":
              row, column = -1, -1
              reasons.append("illegal adjacencies")
              reason = reasons[-1]
              break

        for x in range(column + 1, column + len(word)): #check left column to ensure there are no words above the path
          if x >= 20:
            reasons.append("reaches outside grid")
            reason = reasons[-1]
            break

          elif row - 1 < 0: # past board boundaries, exit the loop, nothing further to check for
            break

          elif row - 1 >= 0: #check for illegal adjacencies
            if board[row - 1][x] != " ":
              reasons.append("illegal adjacencies")
              reason = reasons[-1]
              break

        for x in range(column + 1, column + len(word)): #check below row to ensure there are no letters below the path
          if x >= 20:
            reasons.append("reaches outside grid")
            reason = reasons[-1]
            break

          elif row + 1 >= 20: # past board boundaries, exit the loop, nothing further to check for
            break

          elif row + 1 < 20:
            if board[row + 1][x] != " ": #check for illegal adjacencies
              reasons.append("illegal adjacencies")
              reason = reasons[-1]
              break

        if reason == "":
          for c in range(len(word)):
            board[row][column + c] = word[c]

          return (True, board, "")

      reason = ""

      if board[a][b] == word[-1]: # word will be placed to the left
        row, column = a, b

        if (column + 1) < 20:
          if board[row][column + 1] != " ": #check if there is a letter to the direct right of the row found
            reasons.append("illegal adjacencies")
            reason = reasons[-1]

        for x in range(column - 1, column - len(word) - 1, -1): #check current row to ensure path is clear and ensure there is a space after the path
          if x < -1:  # '<' used and not '<=' to take the space into consideration should a word be placed on board limits
            reasons.append("reaches outside grid")
            reason = reasons[-1]
            break

          elif x > -1: #board cannot have less than a '0' index
            if board[row][x] != " ": #check for illegal adjacencies
              reasons.append("illegal adjacencies")
              reason = reasons[-1]
              break

        for x in range(column - 1, column - len(word), -1): #check above row to ensure there are no letters above the path
          if x <= -1:
            reasons.append("reaches outside grid")
            reason = reasons[-1]
            break

          elif row - 1 < 0: # past board boundaries, exit the loop, nothing further to check for
            break

          elif row - 1 >= 0: #check for illegal adjacencies
            if board[row - 1][x] != " ":
              reasons.append("illegal adjacencies")
              reason = reasons[-1]
              break

        for x in range(column - 1, column - len(word), -1): #check below row to ensure there are no letters below the path
          if x <= -1:
            reasons.append("reaches outside grid")
            reason = reasons[-1]
            break

          elif row + 1 >= 20: # past board boundaries, exit the loop, nothing further to check for
            break

          elif row + 1 < 20: #check for illegal adjacencies
            if board[row + 1][x] != " ":
              reasons.append("illegal adjacencies")
              reason = reasons[-1]
              break

        if reason == "":
          for c in range(len(word) - 1, -1, -1):
            board[row][column] = word[c]
            column -= 1

          return (True, board, "")

      reason = ""

      if board[a][b] in word: # word will be placed centered to another word
        for letter in word:
          if letter == board[a][b]:
            row, column = a, b

            for x in range(column + 1, column + len(word) - word.index(board[row][column]) + 1): #check current row heading rightward to ensure there are no letters in path also ensure there is a space after path
              if x > 20:  # '>' used and not '>=' to take the space into consideration should a word be placed on board limits
                reasons.append("reaches outside grid")
                reason = reasons[-1]
                break

              elif x < 20: #board cannot have more than a '19' index
                if board[row][x] != " ": #check for illegal adjacencies
                  reasons.append("illegal adjacencies")
                  reason = reasons[-1]
                  break

            for x in range(column + 1, column + len(word) - word.index(board[row][column])): #check above row on the right to ensure there are no letters above the path
              if x >= 20:
                reasons.append("reaches outside grid")
                reason = reasons[-1]
                break

              elif row - 1 < 0: # past board boundaries, exit the loop, nothing further to check for
                break

              elif row - 1 >= 0: #check for illegal adjacencies
                if board[row - 1][x] != " ":
                  reasons.append("illegal adjacencies")
                  reason = reasons[-1]
                  break
            
            for x in range(column + 1, column + len(word) - word.index(board[row][column])): #check below row on the right to ensure there are no letters below the path
              if x >= 20:
                reasons.append("reaches outside grid")
                reason = reasons[-1]
                break

              elif row + 1 >= 20: # past board boundaries, exit the loop, nothing further to check for
                break

              elif row + 1 < 20:
                if board[row + 1][x] != " ": #check for illegal adjacencies
                  reasons.append("illegal adjacencies")
                  reason = reasons[-1]
                  break

            for x in range(column - 1, column - word.index(board[row][column]) - 2, -1): #check current row heading leftward to ensure there are no letters in path and ensure there is a space after the word path
              if x < -1: # '<' used and not '<=' to take the space into consideration should a word be placed on board limits
                reasons.append("reaches outside grid")
                reason = reasons[-1]
                break

              elif x > -1: #board cannot have less than a '0' index
                if board[row][x] != " ": #check for illegal adjacencies
                  reasons.append("illegal adjacencies")
                  reason = reasons[-1]
                  break

            for x in range(column - 1, column - word.index(board[row][column]) - 1, -1): #check above row on the left to ensure there are no letters above of path
              if x <= -1:
                reasons.append("reaches outside grid")
                reason = reasons[-1]
                break
                
              elif row - 1 < 0: # past board boundaries, exit the loop, nothing further to check for
                break
              
              elif row - 1 >= 0: #check for illegal adjacencies
                if board[row - 1][x] != " ":
                  reasons.append("illegal adjacencies")
                  reason = reasons[-1]
                  break

            for x in range(column - 1, column - word.index(board[row][column]) - 1, -1):#check below row on the left to ensure there are no letters below the path
              if x <= -1:
                reasons.append("reaches outside grid")
                reason = reasons[-1]
                break
                
              elif row + 1 >= 20: # past board boundaries, exit the loop, nothing further to check for
                break

              elif row + 1 < 20: #check for illegal adjacencies
                if board[row + 1][x] != " ":
                  reasons.append("illegal adjacencies")
                  reason = reasons[-1]
                  break

            if reason == "":
              column = column - word.index(board[row][column])
              
              for c in range(len(word)):
                board[row][column + c] = word[c]
              
              return (True, board, "")
  
  if "illegal adjacencies" in reasons and "reaches outside grid" in reasons: #if an illegal adjacency was encountered, it means a letter to match the word to the placed on the board but it cannot be placed because it was illegal
    return (False, board, "some coordinates are illegal adjacencies and some coordinates reach outside grid")
  
  elif "reaches outside grid" in reasons:
    return (False, board, "all tested cordinates reach outside grid")

  elif "illegal adjacencies" in reasons:
    return (False, board, "all tested cordinates have illegal adjacencies")

  else: #if there are no letters on the board to fit the word return this
    return (False, board, "no matching letter found")

def addVerticalWord(word, board):
  row, column = -1, -1
  reasons = [] # will store any logic errors whuch may occur along the way
  
  for a in range(len(board)):
    for b in range(len(board[a])):
      reason = "" # variable local to loops to store any reason why the word won't be placed on grid
      
      if board[a][b] == word[0]: #word will be placed downwards
        row, column = a, b

        if (row - 1) >= 0:
          if board[row - 1][column] != " ": #check if there is a letter behind the first letter to be used
            reasons.append("illegal adjacencies")
            reason = reasons[-1]
        
        for x in range(row + 1, row + len(word) + 1): #check current column to ensure there are no letters in the path and ensure there is a space after path
          if x > 20:  # '>' used and not '>=' to take the space into consideration should a word be placed on board limits
            reasons.append("reaches outside grid")
            reason = reasons[-1]
            break

          elif x < 20: #board cannot have more than 20 indexes
            if board[x][column] != " ": #check for illegal adjacencies
              reasons.append("illegal adjacencies")
              reason = reasons[-1]
              break

        for x in range(row + 1, row + len(word)): #check left column to ensure there are no words left of path
          if x >= 20:
            reasons.append("reaches outside grid")
            reason = reasons[-1]
            break

          elif column - 1 < 0: # past board boundaries, exit the loop, nothing further to check for
            break

          elif column - 1 >= 0: #check for illegal adjacencies
            if board[x][column - 1] != " ":
              reasons.append("illegal adjacencies")
              reason = reasons[-1]
              break

        for x in range(row + 1, row + len(word)): #check right column to ensure there are no words right of path
          if x >= 20:
            reasons.append("reaches outside grid")
            reason = reasons[-1]
            break

          elif column + 1 >= 20: # past board boundaries, exit the loop, nothing further to check for
            break

          elif column + 1 < 20: #check for illegal adjacencies
            if board[x][column + 1] != " ":
              reasons.append("illegal adjacencies")
              reason = reasons[-1]
              break

        if reason == "":
          for c in range(len(word)):
            board[row + c][column] = word[c]

          return (True, board, "")

      reason = ""

      if board[a][b] == word[-1]: #word will be placed upwards
        row, column = a, b

        if (row + 1) < 20:
          if board[row + 1][column] != " ": #check if there is a letter directly below the column found
            reasons.append("illegal adjacencies")
            reason = reasons[-1]

        for x in range(row - 1, row - len(word) - 1, -1): #check current column to ensure path is clear and ensure there is a space after path
          if x < -1:  # '<' used and not '<=' to take the space into consideration should a word be placed on board limits
            reasons.append("reaches outside grid")
            reason = reasons[-1]
            break

          elif x > -1: #board's smallest index is 0
            if board[x][column] != " ": #check for illegal adjacencies
              reasons.append("illegal adjacencies")
              reason = reasons[-1]
              break

        for x in range(row - 1, row - len(word), -1): #check left column to ensure there are no letters left of path
          if x <= -1:
            reasons.append("reaches outside grid")
            reason = reasons[-1]
            break

          elif column - 1 < 0: # past board boundaries, exit the loop, nothing further to check for
            break

          elif column - 1 >= 0: #check for illegal adjacencies
            if board[x][column - 1] != " ":
              reason = "illegal adjacencies"
              reason = reasons[-1]
              break

        for x in range(row - 1, row - len(word), -1): #check right column to ensure there are no letters to the right of path
          if x <= -1:
            reasons.append("reaches outside grid")
            reason = reasons[-1]
            break
            
          elif column + 1 >= 20: # past board boundaries, exit the loop, nothing further to check for
            break

          elif column + 1 < 20: #check for illegal adjacencies
            if board[x][column + 1] != " ":
              reasons.append("illegal adjacencies")
              reason = reasons[-1]
              break

        if reason == "":
          for c in range(len(word) - 1, -1, -1):
            board[row][column] = word[c]
            row -= 1

          return (True, board, "")

      reason = ""
      
      if board[a][b] in word: # word will be placed centered to another word
        for letter in word:
          if letter == board[a][b]:
            row, column = a, b

            for x in range(row + 1, row + len(word) - word.index(board[row][column]) + 1): #check path to ensure it is clear also ensure there is a space after path
              if x > 20:  # '>' used and not '>=' to take the space into consideration should a word be placed on board limits
                reasons.append("reaches outside grid")
                reason = reasons[-1]
                break

              elif x < 20: #board cannot have more than 20 indexes
                if board[x][column] != " ":
                  reasons.append("illegal adjacencies")
                  reason = reasons[-1]
                  break

            for x in range(row + 1, row + len(word) - word.index(board[row][column])): #check bottom left column to ensure left of path is clear
              if x >= 20:
                reasons.append("reaches outside grid")
                reason = reasons[-1]
                break

              elif column - 1 < 0: # past board boundaries, exit the loop, nothing further to check for
                break
                
              elif column - 1 >= 0: #check for illegal adjacencies
                if board[x][column - 1] != " ":
                  reasons.append("illegal adjacencies")
                  reason = reasons[-1]
                  break
            
            for x in range(row + 1, row + len(word) - word.index(board[row][column])): #check bottom right column to ensure right of path is clear
              if x >= 20:
                reasons.append("reaches outside grid")
                reason = reasons[-1]
                break

              elif column + 1 >= 20: # past board boundaries, exit the loop, nothing further to check for
                break

              elif column + 1 < 20:
                if board[x][column + 1] != " ":
                  reason = "illegal adjacencies"
                  reason = reasons[-1]
                  break

            for x in range(row - 1, row - word.index(board[row][column]) - 2, -1): #check above current column to ensure path is clear and ensure there is a space after path
              if x < -1:  # '<' used and not '<=' to take the space into consideration should a word be placed on board limits
                reasons.append("reaches outside grid")
                reason = reasons[-1]
                break

              elif x > -1: #board's smallest index is 0
                if board[x][column] != " ":
                  reason = "illegal adjacencies"
                  reason = reasons[-1]
                  break

            for x in range(row - 1, row - word.index(board[row][column]) - 1, -1): #check upper left column to ensure left of path is clear
              if x <= -1:
                reasons.append("reaches outside grid")
                reason = reasons[-1]
                break

              elif column - 1 < 0: # past board boundaries, exit the loop, nothing further to check for
                break

              elif column - 1 >= 0: #check for illegal adjacencies
                if board[x][column - 1] != " ":
                  reasons.append("illegal adjacencies")
                  reason = reasons[-1]
                  break

            for x in range(row - 1, row - word.index(board[row][column]) - 1, -1):#check upper right column to ensure right of path is clear
              if x <= -1:
                reasons.append("reaches outside grid")
                reason = reasons[-1]
                break

              elif column + 1 >= 20: # past board boundaries, exit the loop, nothing further to check for
                break
                
              elif column + 1 < 20: #check for illegal adjacencies
                if board[x][column + 1] != " ":
                  reasons.append("illegal adjacencies")
                  reason = reasons[-1]
                  break

            if reason == "":
              row = row - word.index(board[row][column])
              
              for c in range(len(word)):
                board[row + c][column] = word[c]

              return (True, board, "")
  
  if "illegal adjacencies" in reasons and "reaches outside grid" in reasons: #if an illegal adjacency was encountered, it means a letter to match the word to the placed on the board but it cannot be placed because it was illegal
    return (False, board, "some coordinates are illegal adjacencies and some coordinates reach outside grid")
  
  elif "reaches outside grid" in reasons:
    return (False, board, "all tested cordinates reach outside grid")

  elif "illegal adjacencies" in reasons:
    return (False, board, "all tested cordinates have illegal adjacencies")

  else: #if there are no letters on the board to fit the word return this
    return (False, board, "no matching letter found")

def printboard(board):
  output = " 0123456789ABCDEFGHIJ \n "
  
  for i in range(len(board)):
    output += "_"
    
  output += "\n"
  
  for a in range(len(board)): #iterate rows in board
    output += "|"
    
    for b in range(len(board[a])): #iterate columns in board
      output += board[a][b]
      
    output += "|" + str(a) + "\n"
    
  output += " "
  
  for i in range(len(board)):
    output += "_"
    
  output += "\n 0123456789ABCDEFGHIJ\n\n"
  
  output += "A - 10\n"
  output += "B - 11\n"
  output += "C - 12\n"
  output += "D - 13\n"
  output += "E - 14\n"
  output += "F - 15\n"
  output += "G - 16\n"
  output += "H - 17\n"
  output += "I - 18\n"
  output += "J - 19\n\n"

  printfile = open("output.txt", "a") #print to file
  printfile.write(output)
  printfile.close()



words = ["addle", "apple", "clowning", "incline", "plan", "burr"]

words1 = ["alakazam", "arceus", "eevee", "gallade", "luxray", "zamazenta", "gyarados", "giratina", "roserade", "abra", "togepi", "piplup", "lugia", "dialga", "palkia", "mewtwo", "mew", "articuno", "moltres", "uxie", "mespirit", "azelf", "shinx", "ralts", "shaymin", "1"]

crossword(words)
crossword(words1)
