#include<iostream>
#include<algorithm>
#include<vector>
#include<cstdio>
#include<cstdlib>
#include <time.h>  
#include <set>
#define N 120
const int MAX_ITER=10000;
using namespace std;
vector<int> queenPos;
void print(vector<int> data){
	for(int i = 0;i<data.size();i++){
		for(int j=0;j<N;j++){
			if(j==data[i]){
				printf(" * ");
			}
			else{ printf(" - ");}
		}
		printf("\n");
	}
}
vector<int> initializeQueens(){
	srand (time(NULL));
	vector<int> queenPos;
	for(int i=0;i<N;i++){
		queenPos.push_back(rand() % N);
	}
	return queenPos;
}
bool isInConflict(int x1, int y1, int x2, int y2){
	if(y1-x1==y2-x2){
		return true;
	}

	if(y1+x1==y2+x2){
		return true;
	}
	if(y1==y2){
		return true;
	}
	return false;
}
vector<int> getConflicted(){
	set<int> conflicted;
	vector<int> allConflicted;
	for(int i = 1;i<N;i++){
		for(int j = 0;j<i;j++){
		
			if(isInConflict(i,queenPos[i],j,queenPos[j])){
				conflicted.insert(i);
				conflicted.insert(j);
			}
		}
	}
	set<int>::iterator iterator;
	for(iterator = conflicted.begin();
		iterator != conflicted.end();
		iterator++){
			allConflicted.push_back(*iterator);
	}

	return allConflicted;
}

int selectRandomPos(vector<int> array){
	int x = rand()%array.size();
	return x;
}

int main(){
	int foundSolution=false;
	while(!foundSolution){
		queenPos=initializeQueens();
		int iter=0;
		while(iter<MAX_ITER){
			iter++;
			vector<int> conflicted = getConflicted();
			if(conflicted.size()==0){
				foundSolution=true;
				break;
			}
			int randomQueen = conflicted[rand()%conflicted.size()];
			int globalConflicts=N;
			vector<int> minConflicted;
			/// i - column pos of chosen queen
			for(int i=0;i<N;i++){
				int localConflicts=0;

				for(int j=0;j<N;j++){
					if(j!=randomQueen){
						if(isInConflict(randomQueen,i,j,queenPos[j])){
							localConflicts++;
						}
					}
				}
				// new best conflicts was found
				if(localConflicts<globalConflicts){
					globalConflicts=localConflicts;
					minConflicted.clear();

				}
				if(localConflicts==globalConflicts){
					minConflicted.push_back(i);
				}
			}
			// chooses the random pos candidate
			int chosenPos = minConflicted[rand()%minConflicted.size()];
			queenPos[randomQueen]=chosenPos;
		}
		if(foundSolution){
			print(queenPos);
		}
	}
	return 0;
}
