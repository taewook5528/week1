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

int main() {
	Vector2D<int> int_vector(3.5, 2.5);
	int_vector.print();

	Vector2D<float> float_vector(3.5f, 2.5f);
	float_vector.print();

	Vector2D<float> double_vector(3.5, 2.5);
	double_vector.print();

	return 0;
}