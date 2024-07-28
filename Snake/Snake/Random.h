#ifndef RANDOM_H
#define RANDOM_H

#include <random>

namespace Random {
    inline std::mt19937& get_mt() {
        static std::random_device rd;
        static std::mt19937 mt(rd());
        return mt;
    }

    // Function to initialize the random number generator
    inline void init() {
        get_mt();
    }

    // Function to generate a random integer between min and max (inclusive)
    inline int get(int min, int max) {
        std::uniform_int_distribution<int> dist(min, max);
        return dist(get_mt());
    }
}

#endif
