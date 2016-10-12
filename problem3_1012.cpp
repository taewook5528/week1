#include <GLFW/glfw3.h>
#include <cstring>
#include <stdlib.h>		  // srand, rand
#include <thread>         // std::this_thread::sleep_for
#include <chrono>         // std::chrono::seconds
#include "math.h"
#include <windows.h>
#include <vector>
#include <iostream>
#define pi 3.14159265358979

const int width = 866;
const int height = 568;

float* pixels = new float[width*height * 3];// 픽셀의 배열을 선언해줍니다. 해상도 x 3(rgb)입니다.
double xpos, ypos;

void drawPixel(const int& i, const int& j, const float& red, const float& green, const float& blue)//픽셀배열에 값(rgb)을 넣는 과정입니다.
{
	pixels[(i + width* j) * 3 + 0] = red;
	pixels[(i + width* j) * 3 + 1] = green;
	pixels[(i + width* j) * 3 + 2] = blue;
}

void drawLine(const int& i0, const int& j0, const int& i1, const int& j1, const float& red, const float& green, const float& blue)
{

	if (i1 - i0 == 0)//직선 함수는 분모가 0이면 즉 x2-x1 차이가 0이면 오류가 나므로 0일때 y축만그리는것으로함.
	{
		for (int i = j0; i < j1; i++)
		{
			drawPixel(i1, i, red, green, blue);
		}
	}
	else if ((j1 - j0) != 0)//기울기가 0이아닌 나머지일경우 
	{
		for (int i = i0; i < i1; i++)
		{
			const int j = (j1 - j0)*(i - i0) / (i1 - i0) + j0;

			drawPixel(i, j, red, green, blue);// i부터 j까지 rgb값으로 그린다
			drawPixel(i, j - 1, red, green, blue); drawPixel(i, j + 1, red, green, blue);//기울어지면 픽셀수가 적어지기때문에 추가해주었습니다.

		}
	}
	else//가로일경우 
	{
		for (int i = i0; i < i1; i++)
		{
			const int j = (j1 - j0)*(i - i0) / (i1 - i0) + j0;

			drawPixel(i, j, red, green, blue);// i부터 j까지 rgb값으로 그린다

		}
	}
}

void drawcircle(const int& x1, const int& y1, const int& r, const float& red, const float& green, const float& blue)
{
	float i = 0.0;
	float rad_to_deg = 0.0;
	float degree = 360.0;
	int x2 = 0, y2 = 0;

	for (degree = 0; degree < 360; degree++)
	{
		rad_to_deg = degree*(pi / 180);
		x2 = x1 + r*sin(rad_to_deg);
		y2 = y1 + r*cos(rad_to_deg);
		drawPixel(x2, y2, red, green, blue);
		drawPixel(x2 + 1, y2, red, green, blue);
		drawPixel(x2, y2 - 1, red, green, blue);
	}
}

void drawsquare(const int& area, const int& i0, const int& j0, const float& red, const float& green, const float& blue)
{
	drawLine(i0, j0, i0 + area, j0, red, green, blue);
	drawLine(i0 + area, j0, i0 + area, j0 + area, red, green, blue);
	drawLine(i0, j0 + area, i0 + area, j0 + area, red, green, blue);
	drawLine(i0, j0, i0, j0 + area, red, green, blue);

}

class Box
{
public:
	void doSomething()
	{
		drawcircle(700, 400, 100, 1.0f, 0.0f, 0.0f);
	}
};

class Circle
{
public:
	void doSomething()
	{
		drawsquare(170, 400, 320, 1.0f, 0.0f, 0.0f);
	}
};

class GeometricObjectInterface
{
public:
	virtual void doSomething() = 0;
};
template<class T_OPERATION>
class GeometricObject :public GeometricObjectInterface
{
public:
	void doSomething()
	{
		T_OPERATION operation;
		operation.doSomething();
	}
};

std::vector<GeometricObjectInterface*> obj_list;
void drawOnPixelBuffer()
{

	std::fill_n(pixels, width*height * 3, 1.0f);	// white background

	obj_list.push_back(new GeometricObject<Box>);
	obj_list.push_back(new GeometricObject<Circle>);

	for (auto itr : obj_list)
		itr->doSomething();

}

int main(void)
{

	GLFWwindow* window;

	/* Initialize the library */
	if (!glfwInit())
		return -1;

	/* Create a windowed mode window and its OpenGL context */
	window = glfwCreateWindow(width, height, "Hello World", NULL, NULL);
	if (!window)
	{
		glfwTerminate();
		return -1;
	}

	/* Make the window's context current */
	glfwMakeContextCurrent(window);
	glClearColor(1, 1, 1, 1); // while background

							  /* Loop until the user closes the window */
	while (!glfwWindowShouldClose(window))
	{
		glfwGetCursorPos(window, &xpos, &ypos);

		/* Render here */
		glClear(GL_COLOR_BUFFER_BIT);
		drawOnPixelBuffer();

		//TODO: RGB struct

		//Make a pixel drawing function
		//Make a line drawing function
		glDrawPixels(width, height, GL_RGB, GL_FLOAT, pixels);

		/* Swap front and back buffers */
		glfwSwapBuffers(window);

		/* Poll for and process events */
		glfwPollEvents();

		std::this_thread::sleep_for(std::chrono::milliseconds(100));
	}

	glfwTerminate();

	delete[] pixels; // or you may reuse pixels array 

	return 0;
}