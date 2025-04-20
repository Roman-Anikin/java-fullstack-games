#include "web_controller.h"

using namespace s21;

WebController::WebController() : model() {}

void WebController::registerEndpoints(httplib::Server& server) {

    server.Post("/game/snake",
        [this](const auto& req, auto& res) { handleStartGame(req, res); 
    });

    server.Post("/game/snake/action", 
        [this](const auto& req, auto& res) { handleInput(req, res);
    });
    
    server.Patch("/game/snake/move", 
        [this](const auto& req, auto& res) { moveSnake(req, res);
    });
    
    server.Get("/game/snake/state", 
        [this](const auto& req, auto& res) { updateState(req, res);
    });
}

void WebController::handleStartGame(const httplib::Request& req, httplib::Response& res) {
    Game game(req.get_param_value("mode"));
    res.set_content("", "text/plain");
}

void WebController::handleInput(const httplib::Request& req, httplib::Response& res) {
    auto json = json::parse(req.body);
    std::string key = json["key"].get<std::string>();
    model.handleInput(mapKeys(key));
    res.set_content("", "text/plain");
}

void WebController::moveSnake(const httplib::Request& req, httplib::Response& res) {
    model.moveSnake();
    res.set_content("", "text/plain");
}

void WebController::updateState(const httplib::Request& req, httplib::Response& res) {
    auto json = createGameJson();
    res.body = json.dump();
    res.set_header("Content-Type", "application/json");
}

nlohmann::json WebController::createGameJson() {
    nlohmann::json j;
    for (auto& part : model.getSnake().getBody()) {
        j["snake"].push_back({
            {"x", part.getX()},
            {"y", part.getY()}
        });
    };
    j["apple"] = {
        {"x", model.getApple().getX()},
        {"y", model.getApple().getY()}
    };
    j["speed"] = model.getSpeed();
    j["score"] = model.getScore();
    j["highestScore"] = model.getHighestScore();
    j["level"] = model.getLevel();
    j["status"] = mapStatus(model.getStatus());
    return j;
}

int WebController::mapKeys(const std::string& key) {
    if (key == "Enter") return ENTER;
    if (key == " ") return SPACE;
    if (key == "ArrowLeft") return LEFT;
    if (key == "ArrowUp") return UP;
    if (key == "ArrowRight") return RIGHT;
    if (key == "ArrowDown") return DOWN;
    return 0;
}

std::string WebController::mapStatus(GameStatus status) {
    switch(status) {
        case IN_GAME: return "IN_GAME";
        case WIN: return "WIN";
        case LOOSE: return "LOOSE";
        default: return "UNKNOWN";
    }
}