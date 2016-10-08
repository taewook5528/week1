#include <GLFW/glfw3.h>
#include <cstring>
#include <stdlib.h>		  // srand, rand
#include <thread>         // std::this_thread::sleep_for
#include <chrono>         // std::chrono::seconds
#include "shape.h"

using namespace std;

const int width = 1366;//전체 캔버스 가로
const int height = 830;//전체 캔버스 세로

float* pixels = new float[width*height * 3];// 픽셀의 배열 선언. 해상도 x 3(rgb)입니다. 

icon_set* myicons[40];

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
		/* Render here */
		//glClear(GL_COLOR_BUFFER_BIT);
		double xpos, ypos;
		glfwGetCursorPos(window, &xpos, &ypos);
		ypos = height - ypos - 1;
		for (int j = 0; j < 4; j++)
		{
			for (int i = 0; i < 5; i++)
				//myicons[j * 10 + i * 2] = new icon(i * 200 + 100, j * 200 + 100, 70, 3, xpos, ypos);
				myicons[j * 10 + i * 2] = new icon2(i * 200 + 30, j * 200 + 30, i * 200 + 170, j * 200 + 170, 3, xpos, ypos);
		}
		for (int j = 0; j < 2; j++)
		{
			myicons[j * 20 + 1] = new line(80, j * 400 + 260, 120, j * 400 + 340, 3);
			myicons[j * 20 + 3] = new circle(300, j * 400 + 300, 30, 6);
			myicons[j * 20 + 5] = new rect(470, j * 400 + 270, 530, j * 400 + 330, 0);
			myicons[j * 20 + 7] = new draw_X(700, j * 400 + 300);
			myicons[j * 20 + 9] = new draw_A(900, j * 400 + 300);
			myicons[j * 20 + 11] = new arrow_up(100, j * 400 + 100);
			myicons[j * 20 + 13] = new arrow_down(300, j * 400 + 100);
			myicons[j * 20 + 15] = new arrow_left(500, j * 400 + 100);
			myicons[j * 20 + 17] = new arrow_right(700, j * 400 + 100);
			myicons[j * 20 + 19] = new line(900, j * 400 + 70, 900, j * 400 + 130, 0);
		}

		drawOnPixelBuffer(xpos, ypos);

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

void drawPixel(const int& i, const int& j, const float& red, const float& green, const float& blue)
{
	pixels[(i + width* j) * 3 + 0] = red;
	pixels[(i + width* j) * 3 + 1] = green;
	pixels[(i + width* j) * 3 + 2] = blue;
}

void drawLine(int i0, int j0, int i1, int j1, const int t, const float& red, const float& green, const float& blue)
{
	int i, j, k;
	int itmp, jtmp;
	if (i0 > i1)
	{
		itmp = i0;
		i0 = i1;
		i1 = itmp;
		jtmp = j0;
		j0 = j1;
		j1 = jtmp;
	}
	if (i0 == i1)
	{
		if (j0 < j1)
			j = j0;
		else
			j = j1;
		for (; j <= j1 || j <= j0; j++)
			for (k = i0 - t; k <= i0 + t; k++)
				drawPixel(k, j, red, green, blue);
	}
	else
	{
		if (j0 == j1)
		{
			for (i = i0; i <= i1; i++)
				for (k = j0 - t; k <= j0 + t; k++)
					drawPixel(i, k, red, green, blue);
		}
		else
		{
			for (i = i0; i <= i1; i++)
			{
				if (j0 < j1)
					j = j0;
				else
					j = j1;
				for (; j <= j1 || j <= j0; j++)
				{
					if (i == (i1 - i0)*(j - j0) / (j1 - j0) + i0 || j == (j1 - j0)*(i - i0) / (i1 - i0) + j0)
					{
						for (k = -t; k <= t; k++)
						{
							drawPixel(i + k, j, red, green, blue);
							drawPixel(i, j + k, red, green, blue);
						}
					}
				}
			}
		}
	}
}

void drawRect(int x0, int y0, int x1, int y1, int t, float r1, float g1, float b1)
{
	for (int i = x0; i <= x1; i++)
	{
		for (int j = y0; j <= y1; j++)
		{
			if (i <= x0 + t || i >= x1 - t || j <= y0 + t || j >= y1 - t)
			{
				drawPixel(i, j, r1, g1, b1);
			}
		}
	}
}

void drawRectF(const int& i0, const int& j0, const int& i1, const int& j1, const float& red, const float& green, const float& blue)
{
	int i, j;
	for (i = i0; i <= i1; i++)
	{
		for (j = j0; j <= j1; j++)
		{
			drawPixel(i, j, red, green, blue);
		}
	}
}

void drawCirc(const int& i0, const int& j0, const int& r, const int& t, const float& red, const float& green, const float& blue)
{
	int i, j;
	int a, b, c;

	for (i = i0 - r; i <= i0 + r; i++)
	{
		for (j = j0 - r; j <= j0 + r; j++)
		{
			a = ((i - i0)*(i - i0)) + ((j - j0)*(j - j0));
			b = r*r;
			c = (r - t)*(r - t);
			if (a < b&&a >= c)
			{
				drawPixel(i, j, red, green, blue);
			}
		}
	}
}

void drawIcon(const int& i0, const int& j0, const int& r, const int& t, const float& r1, const float& g1, const float& b1, const float& r2, const float& g2, const float& b2, double xpos, double ypos)
{
	int i, j;
	int a, b, c;

	for (i = i0 - r; i <= i0 + r; i++)
	{
		for (j = j0 - r; j <= j0 + r; j++)
		{
			a = ((i - i0)*(i - i0)) + ((j - j0)*(j - j0));
			b = r*r;
			c = (r - t)*(r - t);
			if (a < b&&a >= c)
			{
				if (((xpos - i0)*(xpos - i0)) + ((ypos - j0)*(ypos - j0)) < b)
					drawPixel(i, j, r2, g2, b2);
				else
					drawPixel(i, j, r1, g1, b1);
			}
		}
	}
}

void drawIcon2(int x0, int y0, int x1, int y1, int t, float r1, float g1, float b1, float r2, float g2, float b2, double xpos, double ypos)
{
	for (int i = x0; i <= x1; i++)
	{
		for (int j = y0; j <= y1; j++)
		{
			if (i <= x0 + t || i >= x1 - t || j <= y0 + t || j >= y1 - t)
			{
				if (xpos >= x0 && xpos <= x1 && ypos >= y0 && ypos <= y1)
					drawPixel(i, j, r2, g2, b2);
				else
					drawPixel(i, j, r1, g1, b1);
			}
		}
	}
}

void drawOnPixelBuffer(double xpos, double ypos)
{
	//std::memset(pixels, 1.0f, sizeof(float)*width*height * 3); // doesn't work
	std::fill_n(pixels, width*height * 3, 1.0f);	// white background

	for (int i = 0; i < 40; i++)
		myicons[i]->draw();
	//TODO: try moving object
}