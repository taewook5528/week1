#ifndef __shape_H__
#define __shape_H__

void drawPixel(const int& i, const int& j, const float& red, const float& green, const float& blue);
void drawLine(int i0, int j0, int i1, int j1, const int t, const float& red, const float& green, const float& blue);
void drawRect(int x0, int y0, int x1, int y1, int t, float r1, float g1, float b1);
void drawRectF(const int& i0, const int& j0, const int& i1, const int& j1, const float& red, const float& green, const float& blue);
void drawCirc(const int& i0, const int& j0, const int& r, const int& t, const float& red, const float& green, const float& blue);
void drawIcon(const int& i0, const int& j0, const int& r, const int& t, const float& r1, const float& g1, const float& b1, const float& r2, const float& g2, const float& b2, double xpos, double ypos);
void drawIcon2(int x0, int y0, int x1, int y1, int t, float r1, float g1, float b1, float r2, float g2, float b2, double xpos, double ypos);
void drawOnPixelBuffer(double xpos, double ypos);

class icon_set
{
public:
	virtual void draw()
	{
	}
};

class icon : public icon_set
{
public:
	int x0, y0, r, t;
	double xpos, ypos;
	icon(int _x0, int _y0, int _r, int _t, double _xpos, double _ypos)
	{
		x0 = _x0;
		y0 = _y0;
		r = _r;
		t = _t;
		xpos = _xpos;
		ypos = _ypos;
	}
	void draw();
};

class icon2 : public icon_set
{
public:
	int x0, y0, x1, y1, t;
	double xpos, ypos;
	icon2(int _x0, int _y0, int _x1, int _y1, int _t, double _xpos, double _ypos)
	{
		x0 = _x0;
		y0 = _y0;
		x1 = _x1;
		y1 = _y1;
		t = _t;
		xpos = _xpos;
		ypos = _ypos;
	}
	void draw();
};

class line : public icon_set
{
public:
	int x0, y0, x1, y1, t;
	line(int _x0, int _y0, int _x1, int _y1, int _t)
	{
		x0 = _x0;
		y0 = _y0;
		x1 = _x1;
		y1 = _y1;
		t = _t;
	}
	void draw();
};

class rect : public icon_set
{
public:
	int x0, y0, x1, y1, t;
	rect(int _x0, int _y0, int _x1, int _y1, int _t)
	{
		x0 = _x0;
		y0 = _y0;
		x1 = _x1;
		y1 = _y1;
		t = _t;
	}
	void draw();
};

class f_rect : public icon_set
{
public:
	int x0, y0, x1, y1;
	f_rect(int _x0, int _y0, int _x1, int _y1)
	{
		x0 = _x0;
		y0 = _y0;
		x1 = _x1;
		y1 = _y1;
	}
	void draw();
};

class circle : public icon_set
{
public:
	int x0, y0, r, t;
	circle(int _x0, int _y0, int _r, int _t)
	{
		x0 = _x0;
		y0 = _y0;
		r = _r;
		t = _t;
	}
	void draw();
};

class arrow_up : public icon_set
{
public:
	int x0, y0, x1, y1;
	arrow_up(int _x0, int _y0)
	{
		x0 = _x0;
		y0 = _y0;
	}
	void draw();
};

class arrow_down : public icon_set
{
public:
	int x0, y0, x1, y1;
	arrow_down(int _x0, int _y0)
	{
		x0 = _x0;
		y0 = _y0;
	}
	void draw();
};

class arrow_left : public icon_set
{
public:
	int x0, y0, x1, y1;
	arrow_left(int _x0, int _y0)
	{
		x0 = _x0;
		y0 = _y0;
	}
	void draw();
};

class arrow_right : public icon_set
{
public:
	int x0, y0, x1, y1;
	arrow_right(int _x0, int _y0)
	{
		x0 = _x0;
		y0 = _y0;
	}
	void draw();
};

class draw_X :public icon_set
{
public:
	int x0, y0, x1, y1;
	draw_X(int _x0, int _y0)
	{
		x0 = _x0;
		y0 = _y0;
	}
	void draw();
};

class draw_A :public icon_set
{
public:
	int x0, y0, x1, y1;
	draw_A(int _x0, int _y0)
	{
		x0 = _x0;
		y0 = _y0;
	}
	void draw();
};

#endif __shape_H__