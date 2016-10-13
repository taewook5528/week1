#include <iostream>

template<class T>

class Vector2D {
public:
	T _x, _y;

	Vector2D(const T& x, const T& y)
	{
		_x = x;
		_y = y;
	}

	void print() 
	{
		std::cout << "_x = " << _x << ", _y = " << _y << std::endl;
	}
};
