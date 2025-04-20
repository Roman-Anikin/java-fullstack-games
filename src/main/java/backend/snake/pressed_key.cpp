#pragma once

namespace s21 {
    class PressedKey {
        public:
            int getKey() { return key; }
            void setKey(int newKey) { key = newKey; }
        
        private:
            int key = 0;
    };
}  // namespace s21
