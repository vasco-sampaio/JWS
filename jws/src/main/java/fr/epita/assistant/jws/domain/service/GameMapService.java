package fr.epita.assistant.jws.domain.service;

import fr.epita.assistant.jws.Position;
import fr.epita.assistant.jws.RLE;
import fr.epita.assistant.jws.data.model.GameMapModel;

import fr.epita.assistant.jws.domain.entity.GameMapEntity;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class GameMapService {
    @ConfigProperty(name = "JWS_MAP_PATH") String map_path;

    public GameMapModel getMap(GameMapEntity mapEntity) {
        return GameMapModel.<GameMapModel>findAll()
                .stream()
                .filter(map -> map.id == mapEntity.id)
                .findFirst()
                .orElse(null);
    }

    public GameMapEntity addMap() {
        var list = RLE.wrappDecode(map_path);

        StringBuilder buildMap = new StringBuilder();
        list.forEach(buildMap::append);
        var mModel = new GameMapModel().withMap(new String(buildMap));

        GameMapModel.persist(mModel);

        return new GameMapEntity(mModel.id, mModel.map);
    }

    @Transactional
    public void addBomb(GameMapModel mModel, final Position pos) {
        StringBuilder builder = new StringBuilder(mModel.map.substring(0, pos.posY * 17 + pos.posX));
        builder.append('B');
        builder.append(mModel.map.substring(pos.posY * 17 + pos.posX + 1));

        mModel.map = new String(builder);
    }

    @Transactional
    public void exploseBomb(GameMapModel map, Position pos) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < map.map.length(); ++i) {
                if (i == (pos.posY - 1) * 17 + pos.posX && map.map.charAt(i) == 'W')
                    builder.append('G');
                else if (i == pos.posY * 17 + pos.posX - 1 && map.map.charAt(i) == 'W')
                    builder.append('G');
                else if (i == pos.posY * 17 + pos.posX)
                    builder.append('G');
                else if (i == pos.posY * 17 + pos.posX + 1 && map.map.charAt(i) == 'W')
                    builder.append('G');
                else if (i == (pos.posY + 1) * 17 + pos.posX && map.map.charAt(i) == 'W')
                    builder.append('G');
                else
                    builder.append(map.map.charAt(i));
            }

            map.map = new String(builder);

    }
}
