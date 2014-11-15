#include<iostream>
#include<vector>
#include<cstdio>
#include<map>
#include<queue>
#include<stack>
#include<algorithm>
#include<cmath>
#define LIMIT 25
using namespace std;
vector<pair<int,int> > dirs;
stack<pair<int,int>> path;
int matrix[5][5]=  {{0,0,0,0,0},
					{-1,-1,0,-1,0},
					{0,-1,-1,-1,0},
					{0,0,0,0,0},
					{0,0,0,0,0}};
int n=7,m=7;

bool found;

void add_dir(int x,int y){
	pair<int,int> dira;
	dira.first=x;
	dira.second=y;
	dirs.push_back(dira);
}

bool isValid(int matrix[][5] , int x, int y){
	if(x<0||y<0||x>=5||y>=5){
	return false;
	}
	if(matrix[x][y]==-1){
	return false;
	}
	if(matrix[x][y]==1){
	return false;
	}

	return true;

}
void printmatrix( int matrix[][5]){
	for(int i=0;i<5;i++){
		for(int j = 0;j<5;j++){
		printf("%3d",matrix[i][j]);}
		cout<<endl;
	}
	cout<<"-------------"<<endl;
}
void print(stack<pair<int,int> > path){
	stack<pair<int,int> > pathRev;
	while(!path.empty()){
		pathRev.push(path.top());
		path.pop();
	}
	while(!pathRev.empty()){
		pair<int,int> step=pathRev.top();
		printf("step %d %d\n", step.first, step.second);
		pathRev.pop();
	}
}
bool find_dfspath(int maxDepth, pair<int,int> curPos, pair<int,int> endPos, int curDepth){
	if(curDepth>maxDepth){
	return false;
	}
	if(curPos == endPos){
		return true;
	}
	for(int neigh=0;neigh<4;neigh++){

		pair<int,int> dir= dirs[neigh];
		int newx= dirs[neigh].first+curPos.first;
		int newy = dirs[neigh].second+curPos.second;
		if(isValid(matrix, newx, newy )){
		matrix[newx][newy]=1;
		//printmatrix(matrix);
		pair<int,int> nextCur;
		nextCur.first=newx;
		nextCur.second=newy;
		path.push(nextCur);
		if(find_dfspath(maxDepth,nextCur, endPos, curDepth+1)==true){
			printmatrix(matrix);
			print(path);
			system("PAUSE");
			exit(0);
		}
		path.pop();
		matrix[newx][newy]=0;

		}
	}
}
void initialize(){
	add_dir(1,0);
	add_dir(0,1);
	add_dir(-1,0);
	add_dir(0,-1);
}

int main(){
	//scanf("%d%d", &startx,&starty);
	//scanf("%d%d", &endx,&endy);
	pair<int,int>startpos;
	startpos.first=0;
	startpos.second=0;
pair<int,int>endpos;
	endpos.first=4;
	endpos.second=0;
	initialize();

	path.push(startpos);

	for(int maxdepth=1;maxdepth<=LIMIT;maxdepth++){
		found = find_dfspath(maxdepth,startpos,endpos, 0);
	}
return 0;
}
