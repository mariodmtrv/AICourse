/*
@author Mario Dimitrov
@id 80715
*/
#include<iostream>
#include<algorithm>
#include<vector>
#include<set>
#include<cmath>
#include<bitset>
#include<ctime>
using namespace std;

class Point{
	int x;
	int y;
public:
	Point(int _x, int _y){
		this->x = _x;
		this->y = _y;
	}
	int getX(){
		return this->x;
	}
	int getY(){
		return this->y;
	}
	double getDist(Point other){
		return sqrt((this->x - other.x)*(this->x - other.x) +
			(this->y - other.y)*(this->y - other.y));
	}
	void print(){
		printf("[ %d, %d], ", x, y);
	}
};
class Chromosome{
private:
	vector<int> pointIndices;
	static const int GENES_COUNT = 7;
	double fitness;
	void addCrossGene(set<int>&used, Chromosome& parent, int index){
		int itemInd = parent.pointIndices[index];

		if (used.find(itemInd) == used.end()){
			addGene(index, itemInd);
			used.insert(itemInd);
		}
		else {
			//could not add gene, will deal later
			addGene(index, -1);
		}
	}
public:
	Chromosome(){
		pointIndices.resize(GENES_COUNT);
	}
	void mutate(){
		int randNum = rand() % GENES_COUNT;
		swap(pointIndices[randNum], pointIndices[GENES_COUNT - randNum - 1]);
	}
	void fill(){
		for (int i = 0; i < GENES_COUNT; i++){
			pointIndices[i] = i;
		}
	}
	void shuffleGenes(){
		std::random_shuffle(pointIndices.begin(), pointIndices.end());
	}
	void fillEmptyGenes(set<int>&used){
		for (int geneIndex = 0; geneIndex < GENES_COUNT; geneIndex++){
			if (this->pointIndices[geneIndex] == -1){
				for (int gene = 0; gene < GENES_COUNT; gene++){
					if (used.find(gene) == used.end()){
						used.insert(gene);
						this->pointIndices[geneIndex] = gene;
						break;
					}
				}
			}
		}
	}
	void addGene(int pos, int geneIndex){
		this->pointIndices[pos] = geneIndex;
	}
	/* Crossing
	in 2 points
	*/
	vector<Chromosome> crossing(Chromosome other){
		//choose 2 points and perform the crossing
		// each element is unique in the result
		int posA = rand() % (GENES_COUNT - 1);
		int posB = rand() % (GENES_COUNT - 1 - posA) + posA + 1;
		Chromosome childA, childB;
		set<int> childAUsed, childBUsed;
		// copies from this 
		for (int i = 0; i <= posA; i++){
			childA.addCrossGene(childAUsed, *this, i);
			childB.addCrossGene(childBUsed, other, i);
		}
		// crosses with the other parent
		for (int i = posA + 1; i <= posB; i++){
			childA.addCrossGene(childAUsed, other, i);
			childB.addCrossGene(childBUsed, *this, i);
		}
		// copies from this
		for (int i = posB + 1; i < GENES_COUNT; i++){
			childA.addCrossGene(childAUsed, *this, i);
			childB.addCrossGene(childBUsed, other, i);
		}

		childA.fillEmptyGenes(childAUsed);
		childB.fillEmptyGenes(childBUsed);

		vector<Chromosome>children;
		children.push_back(childA);
		children.push_back(childB);
		return children;
	}
	void partiallyMapCrossover(){
		// choose 
	}
	vector<Chromosome> cyclicCrossover(Chromosome other){
		// cycles in the permutation child a gets from p a ch b from p b
		// second permutation swap children
		Chromosome childA, childB;
		set<int> parentAUsed;
		set<int> parentBUsed;
		int pos = 1;
		int count = 0;
		int indexB = 1;
		while (true){
			int posB = this->pointIndices[pos];
			int posA = 0;
			childA.addGene(count, posB);
			childB.addGene(count, posA);
		}
		vector<Chromosome>children;
		children.push_back(childA);
		children.push_back(childB);
		return children;

	}
	/**
	min sum of distances in the sequence
	*/
	void getFitness(vector<Point> pointValues){
		double fitness = 0.0;
		for (int i = 1; i < pointIndices.size(); i++){
			fitness += pointValues[pointIndices[i]].getDist(pointValues[pointIndices[i - 1]]);
		}
		this->fitness = fitness;
	}
	bool operator<(Chromosome other){
		return this->fitness < other.fitness;
	}
	void print(vector<Point> pointValues){
		for (int i = 0; i < pointIndices.size(); i++){
			pointValues[pointIndices[i]].print();
			printf("-> ");
		}
		printf("  with fitness %lf", this->fitness);
		printf("\n");
	}
	Chromosome& operator=(const Chromosome& other){
		if (this != &other){
			this->fitness = other.fitness;
			this->pointIndices = other.pointIndices;
		}
		return *this;
	}

};
void addItems(vector<Chromosome>&pop, vector<Chromosome>&newMembers){
	for (int i = 0; i < newMembers.size(); i++){
		pop.push_back(newMembers[i]);
	}
}
class Population{
	vector<Chromosome> population;
	vector<Point> pointValues;
	static const set<int> keyIters;
	static const int MAX_ITER;
	static const double MUT_PROB;
	static const int POPULATION_SIZE;
	static const int BEST_POPULATION;

public:
	Population(vector<Point> pointValues){
		this->pointValues = pointValues;
	}
	void performMutations(){
		//mutation ~3% probability of gene mutation
		for (int i = 0; i < POPULATION_SIZE; i++){
			int choose = (rand() % POPULATION_SIZE) % (int)(POPULATION_SIZE*(1.0/MUT_PROB));
			if (choose==0){
				population[i].mutate();
				population[i].getFitness(pointValues);
			}
		}
	}
	void performCrossings(){
		//30% will be new children 
		//15 crossings
		int crossings = (POPULATION_SIZE - BEST_POPULATION) / 2;
		vector<Chromosome> children;//selected 
		for (int j = 0; j < crossings; j++){
			//choose random parent pairs from the best part of the population
			//and produce children
			int parentA = rand() % BEST_POPULATION;
			int parentB = rand() % BEST_POPULATION;
			vector<Chromosome> siblings = population[parentA].crossing(population[parentB]);
			addItems(children, siblings);
		}
		//delete the worst individuals => children will take their pos
		for (int i = 0; i < children.size(); i++){
			children[i].getFitness(pointValues);
			population[POPULATION_SIZE - children.size() + i] = children[i];
		}
	}
	void initializePopulation(){
		for (int i = 0; i < POPULATION_SIZE; i++){
			population.push_back(Chromosome());
			population[i].fill();
			population[i].shuffleGenes();
		}

	}
	void calculatePopulationFitness(){
		for (int i = 0; i < POPULATION_SIZE; i++){
			population[i].getFitness(pointValues);
		}
	}
	void solve(){
		int generation = 1;
		initializePopulation();
		calculatePopulationFitness();
		while (generation < MAX_ITER){
			sort(population.begin(), population.end());
			if (keyIters.find(generation) != keyIters.end()){
				//prints the top element
				printf("At iteration %d best route is ", generation);
				population[0].print(pointValues);
				printf("\n");
			}
			performCrossings();
			performMutations();
			generation++;
		}
	}
};
const int Population::MAX_ITER = 10e3;
const double  Population::MUT_PROB = 0.03;
const int  Population::POPULATION_SIZE = 100;
const int  Population::BEST_POPULATION = (int)(POPULATION_SIZE*0.7);
const set<int> Population::keyIters = set<int>({1, 10,  50, 100, 200});
int main(){
	srand(time(NULL));
	vector<Point> points = vector<Point>({ Point(0, 0), Point(1, 1), Point(2, 2), Point(3, 3), Point(4, 4), Point(5, 5), Point(6, 6) });
	std::random_shuffle(points.begin(), points.end());
	Population population(points);
	population.solve();
	Chromosome a;
	

}