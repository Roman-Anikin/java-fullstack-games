#pragma once

#include "model.h"
#include "game.h"
#include "httplib.h"
#include <functional>
#include <string>
#include "json.hpp"

using json = nlohmann::json;

namespace s21 {

class WebController {
    
 public:
  WebController();

  using Handler = std::function<void(const httplib::Request&, httplib::Response&)>;

  void registerEndpoints(httplib::Server& server);

 private:
  Model model;

  void handleStartGame(const httplib::Request& req, httplib::Response& res);
  void handleInput(const httplib::Request& req, httplib::Response& res);
  void moveSnake(const httplib::Request& req, httplib::Response& res);
  void updateState(const httplib::Request& req, httplib::Response& res);
  nlohmann::json createGameJson();
  int mapKeys(const std::string& key);
  std::string mapStatus(GameStatus status); 
};

}  // namespace s21