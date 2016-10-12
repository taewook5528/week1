#include <iostream>
#include <string>

template<class T_CLASS>

void print(const T_CLASS& i)
{
	std::cout << i << std::endl;
}

int main(void)
{
	print(1);
	print(2.345f);
	print("hello world");

	return 0;
}