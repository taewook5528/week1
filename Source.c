#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <stdbool.h>
double getRandomNumber(const double min, const double max)//min에서 max 사이의 난수 출력
{
	double temp = (double)rand() / (double)RAND_MAX;//0~1사이의 난수 생성
	temp = min + (max - min)*temp;//min에서 max 사이의 난수 생성
	return temp;//min에서 max 사이의 난수 출력
}
bool isInsideCircle(const double x, const double y)
{
	const double x_c = 0.5;
	const double y_c = 0.5;
	const double r = 0.5;
	double f = (x - x_c)*(x - x_c) + (y - y_c)*(y - y_c) - r*r;

	if (f > 0.0)
		return false;
	else
		return true;
}


int main(void)
{
	FILE *of = fopen("circle.txt", "w");

	srand((unsigned int)time(NULL));

	for (int i = 0; i < 10000; i++)
	{
		double x = getRandomNumber(0.0, 1.0);
		double y = getRandomNumber(0.0, 1.0);

		if (isInsideCircle(x, y) == true)
			fprintf(of, "%f, %f\n", x, y);
	}

	fclose(of);
	return 0;
}