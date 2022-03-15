def evaluate(hand):
    rank = []
    suit = []
    flush = True
    rank_count = 1 #The rank_count is 1 because the first occurence of the card has to be accounted for
    first_rank, second_rank = "", ""
    current_highest = ""
    order = ["2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"]
    
    #Separate rank from suit in parallel lists
    for position in range(len(hand)):
        if position % 2 == 0: #An element at this position is the rank
            if hand[position].isalpha() and hand[position].islower(): #If the user enters an alpha lowercase rank, turn it to uppercase
                rank.append(hand[position].upper())
            
            else: #Else just add it to the list
                rank.append(hand[position])
        
        else: #An element at this position is the suit
            suit.append(hand[position].lower())
    
    #Check for four of a kind
    for rank1 in range(len(rank)):
        for rank2 in range((rank1 + 1), len(rank)): #Used if the first occurence of the four of a kind is the second card
            if rank[rank1] == rank[rank2]:
                rank_count += 1 #Increment rank_count once a matching rank is found
    
        if rank_count == 4:
            return "four of a kind"
        
        else: #Reset rank_count and search again until all iterations are complete
            rank_count = 1
    
    #Check full house
    first_rank = rank[0] #Let the first card be the first rank then check for 2 or 3 of the same rank. Then, check to see if the remaining cards are the same rank
    
    for cur_rank in rank:
        if cur_rank != first_rank:
            second_rank = cur_rank
            break
    
    if (rank.count(first_rank) == 2 and rank.count(second_rank) == 3) or (rank.count(first_rank) == 3 and rank.count(second_rank) == 2):
        return "full house"
    
    #Check for flush
    initial_suit = suit[0] #Let the first card be the comparing suit then check to see if every other card is the same suit
    
    for cur_suit in suit:
        if cur_suit != initial_suit:
            flush = False
    
    if flush == True:
        return "flush"
    
    #Check for three of a kind
    for rank1 in range(len(rank)):
        for rank2 in range((rank1 + 1), len(rank)): #Used if the first occurence of the three of a kind is the second or third card
            if rank[rank1] == rank[rank2]:
                rank_count += 1 #Increment rank_count once a matching rank is found
    
        if rank_count == 3:
            return "three of a kind"
               
        else: #Reset rank_count and search again until all iterations are complete
            rank_count = 1
        
    #Check for pair
    for rank1 in range(len(rank)):
        for rank2 in range((rank1 + 1), len(rank)): #Used if the first occurence of the pair is after the first card
            if rank[rank1] == rank[rank2]:
                return "pair"
    
    #Check for highest
    current_highest = rank[0] #Let the first card be the current highest, then traverse the other cards to compare ranks
    
    for cur_rank in rank:
        if order.index(cur_rank) > order.index(current_highest):
            current_highest = cur_rank
            
    return current_highest + " high"
    
print(evaluate("5c5d2d5h5s")) #four of a kind
print(evaluate("6d5c6h5h6c")) #full house
print(evaluate("jd9d5d7d3d")) #flush
print(evaluate("Qs7dQd9cQh")) #three of a kind
print(evaluate("ThQsTcKc4h")) #pair
print(evaluate("2s3hQh8s9d")) #Q high
print("The hand entered has evaluated to:", evaluate(input("Please enter a hand of cards to evaluate: ")))