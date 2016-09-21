#include <GLFW/glfw3.h>
#include <algorithm>

const int width = 640;
const int height = 480;

float* pixels = new float[width*height * 3];

void drawPixel(const int& i, const int& j, const float& red, const float& green, const float& blue)
{
	pixels[(i + width* j) * 3 + 0] = red;
	pixels[(i + width* j) * 3 + 1] = green;
	pixels[(i + width* j) * 3 + 2] = blue;
}

void drawLine(const int& i0, const int& j0, const int& i1, const int& j1, const float& red, const float& green, const float& blue)
{
	for (int i = i0; i <= i1; i++)
	{
		const int j = (j1 - j0)*(i - i0) / (i1 - i0) + j0;

		drawPixel(i, j, red, green, blue);
	}
}

void drawNewLine(const float& red, const float& green, const float& blue)
{//속이 찬 사각형
	for (int i = 0; i < 100; i++) {
		for (int j = 0; j < 100; j++) {
			drawPixel(i+350, j+350, red, green, blue);
		}
	}
}

void drawEmptySqure(const float& red, const float& green, const float& blue)
{//empty Square
	for (int i = 0; i < 100; i++)
	{
		drawPixel(i + 210,210, red, green, blue);
		drawPixel(210, i + 210, red, green, blue);

		drawPixel(310, i+210, red, green, blue);
		drawPixel(210+i, 310, red, green, blue);
	}
}

void drawPentagon(const float& red, const float& green, const float& blue)
{
	drawLine(10, 200, 30, 230, 0.0f, 0.0f, 0.0f);
	drawLine(30, 230, 50, 200, 0.0f, 0.0f, 0.0f);
	drawLine(50, 200, 40, 170, 0.0f, 0.0f, 0.0f);
	drawLine(40, 170, 20, 170, 0.0f, 0.0f, 0.0f);
	drawLine(20, 170, 10, 200, 0.0f, 0.0f, 0.0f);
}

void draw()
{
	///thick line///
	drawLine(10, 100, 100, 150, 0.0f, 0.0f, 1.0f);
	drawLine(11, 101, 101, 151, 0.0f, 0.0f, 1.0f);
	drawLine(12, 102, 102, 152, 0.0f, 0.0f, 1.0f);

	///triangle////
	drawLine(10, 10, 150, 150, 0.0f, 0.0f, 0.0f);//왼쪽변
	drawLine(10, 10, 300, 10, 0.0f, 0.0f, 0.0f);//밑변
	drawLine(150, 150, 300, 10, 0.0f, 0.0f, 0.0f);//오른쪽 변

	///empty square///
	drawEmptySqure(0.0f, 0.0f, 0.0f);
	///squre(not empty)///
	drawNewLine(0.0f, 0.0f, 0.0f);
	//pentagon////
	drawPentagon(0.0f, 0.0f, 0.0f);
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

	/* Loop until the user closes the window */
	while (!glfwWindowShouldClose(window))
	{
		/* Render here */
		glClear(GL_COLOR_BUFFER_BIT);

		std::fill_n(pixels, width*height * 3, 1.0f);	// white background

		draw();

		glDrawPixels(width, height, GL_RGB, GL_FLOAT, pixels);

		/* Swap front and back buffers */
		glfwSwapBuffers(window);

		/* Poll for and process events */
		glfwPollEvents();
	}

	delete[] pixels;

	glfwTerminate();
	return 0;
}