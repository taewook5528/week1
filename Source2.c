#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <stdbool.h>

double getRandomNumber(const double min, const double max)//min���� max ������ ���� ���
{
	double temp = (double)rand() / (double)RAND_MAX;//0~1������ ���� ����
	temp = min + (max - min)*temp;//min���� max ������ ���� ����
	return temp;//min���� max ������ ���� ���
}

bool isInsideCircle(const double x, const double y)
{
	const double x_c = 3.0;
	const double y_c = 2.0;
	const double r = 1.0;
	double f = ((x - x_c)*(x - x_c)/9.0) + ((y - y_c)*(y - y_c)/4.0) - (r*r);

	if (f > 0.0)
		return false;
	else
		return true;
}


int main(void)
{
	FILE *of = fopen("circle2.txt", "w");

	srand((unsigned int)time(NULL));

	for (int i = 0; i < 10000; i++)
	{
		double x = getRandomNumber(0.0, 6.0);
		double y = getRandomNumber(0.0, 4.0);

		if (isInsideCircle(x, y) == true)
			fprintf(of, "%lf, %lf\n", x, y);
	}

	fclose(of);
	return 0;
}