package oldcaptain;

import oldcaptain.pathfinding.GraphGenerator;
import oldcaptain.pathfinding.TileGraph;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * @author fegemo <coutinho@decom.cefetmg.br>
 *     @edited by henriquegesler <henriquegesler@gmail.com> on 29/09/2018
 */

public class LevelManager {
    public static int tileWidth;
    public static int tileHeight;
    public static int horizontalTiles;
    public static int verticalTiles;
    public static int totalPixelWidth;
    public static int totalPixelHeight;
    public static final int maxCost = 9;
    public static TiledMap tiledMap;
    public static TileGraph graph;
    
    public static TiledMap LoadLevel(String file) {
        tiledMap = new TmxMapLoader().load(file);
        
        MapProperties props = tiledMap.getProperties();
        horizontalTiles = props.get("width", Integer.class);
        verticalTiles = props.get("height", Integer.class);
        tileWidth = props.get("tilewidth", Integer.class);
        tileHeight = props.get("tileheight", Integer.class);
        totalPixelWidth = tileWidth * horizontalTiles;
        totalPixelHeight = tileHeight * verticalTiles;
        
        graph =  GraphGenerator.generateGraph(tiledMap);
        
        return tiledMap;
    }
}
