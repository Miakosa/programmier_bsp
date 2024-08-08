#ifndef OBJECT
#define OBJECT

#include <curses.h>

class Object
{
protected:
	int x, y;
	const char* symbol;

public:
	Object(const char* symbol);
	~Object() {}

	virtual void set_position(int x, int y);
	void set_random_position(int x, int y);
	virtual void draw(WINDOW* win) const;
	virtual int get_x() const;
	virtual int get_y() const;
};
#endif 

