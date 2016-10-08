#include <GLFW/glfw3.h>
#include <cstring>
#include <stdlib.h>		  // srand, rand
#include <thread>         // std::this_thread::sleep_for
#include <chrono>         // std::chrono::seconds
#include "shape.h"

using namespace std;

void icon::draw()
{
	drawIcon(x0, y0, r, t, 1, 0, 0, 0, 0, 1, xpos, ypos);
}

void icon2::draw()
{
	drawIcon2(x0, y0, x1, y1, t, 0, 0, 1, 1, 0, 0, xpos, ypos);
}

void line :: draw()
{
	drawLine(x0, y0, x1, y1, t, 0, 0, 0);
}

void rect :: draw()
{
	drawRect(x0, y0, x1, y1, t, 0, 0, 0);
}

void f_rect:: draw()
{
	drawRectF(x0, y0, x1, y1, 0, 0, 0);
}

void circle::draw()
{
	drawCirc(x0, y0, r, t, 0, 0, 0);
}

void arrow_up::draw()
{
	drawLine(x0, y0 - 30, x0, y0 + 30, 0, 0, 0, 0);
	drawLine(x0 - 20, y0 + 10, x0, y0 + 30, 0, 0, 0, 0);
	drawLine(x0, y0 + 30, x0 + 20, y0 + 10, 0, 0, 0, 0);
}

void arrow_down::draw()
{
	drawLine(x0, y0 - 30, x0, y0 + 30, 0, 0, 0, 0);
	drawLine(x0 - 20, y0 - 10, x0, y0 - 30, 0, 0, 0, 0);
	drawLine(x0, y0 - 30, x0 + 20, y0 - 10, 0, 0, 0, 0);
}

void arrow_left::draw()
{
	drawLine(x0 - 30, y0, x0 + 30, y0, 0, 0, 0, 0);
	drawLine(x0 - 30, y0, x0 - 10, y0 - 20, 0, 0, 0, 0);
	drawLine(x0 - 30, y0, x0 - 10, y0 + 20, 0, 0, 0, 0);
}

void arrow_right::draw()
{
	drawLine(x0 - 30, y0, x0 + 30, y0, 0, 0, 0, 0);
	drawLine(x0 + 10, y0 - 20, x0 + 30, y0, 0, 0, 0, 0);
	drawLine(x0 + 10, y0 + 20, x0 + 30, y0, 0, 0, 0, 0);
}

void draw_X::draw()
{
	drawLine(x0 - 30, y0 - 30, x0 + 30, y0 + 30, 0, 0, 0, 0);
	drawLine(x0 - 30, y0 + 30, x0 + 30, y0 - 30, 0, 0, 0, 0);
}

void draw_A::draw()
{
	drawLine(x0 - 30, y0 - 30, x0, y0 + 30, 0, 0, 0, 0);
	drawLine(x0, y0 + 30, x0 + 30, y0 - 30, 0, 0, 0, 0);
	drawLine(x0 - 15, y0, x0 + 15, y0, 0, 0, 0, 0);
}