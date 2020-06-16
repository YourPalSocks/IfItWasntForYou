/*
The MapBuilder class is responsible for reading and interpreting a .tmx Tiled Map thanks to the Tiled Reader java library
        Available at: https://github.com/AlexHeyman/TiledReader
        Doc available at: http://www.alexheyman.org/tiledreader/javadoc/index.html
Interpreting Maps:
 */

//Import to get the TiledReader library
import org.tiledreader.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapBuilder
{
    private int tileSize;
    private org.tiledreader.TiledMap map;
    private TiledTileset mapTileSet;

    public MapBuilder()
    {
        tileSize = 0;
        map = null;
        mapTileSet = null;
    }

    public void loadTileMap(String name)
    {
        if(map == null || mapTileSet == null)
        {
            GameRunner.debugConsole.AddTextToView("Tiled Map " + name + " failed to be loaded");
        }
        //TODO Remove direct reference
        map = TiledReader.getMap("C:\\Users\\ethan\\Desktop\\TestMap\\TestMap.tmx");
        //Get the tileset
        mapTileSet = map.getTilesets().get(0); //Load the first mapset
        tileSize = mapTileSet.getTileHeight(); //Height and Width MUST be the same
    }

    /*
    This method is responsible for loading the tilemap to the JPanel as individual images, gameobjects, and whatever.
    This method combs layer by layer in this order:
    0 -- Background tiles (0 Collision)
    1 -- Collisions tiles
     */
    public void buildMapToPanel(GameFrame toAdd)
    {
        //Get storage variables
        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();

        //Create the bufferedImage
        BufferedImage tileset = null;
        try
        {
            tileset = ImageIO.read(new File(mapTileSet.getImage().getSource()));
        }
        catch (IOException e)
        {
            GameRunner.debugConsole.AddTextToView("tileset buffered image for " + mapTileSet.getName() + " could not load");
            return;
        }
        //Get all tiles
        ArrayTileLayer layer = (ArrayTileLayer) map.getNonGroupLayers().get(0);
        //TODO Cycle through all tiles on layer
        for(int x = 0; x < mapWidth; x++)
        {
            for(int y = 0; y < mapHeight; y++)
            {
                if(layer.getTile(x,y) == null)
                {
                    continue;
                }
                TiledTile t = layer.getTile(x,y);
                Image subImg = tileset.getSubimage(t.getTilesetX() * tileSize, t.getTilesetY() * tileSize, tileSize, tileSize);
                RenderableTile renT = new RenderableTile(subImg, new Position(), 0);
                renT.scale(1.4);
                renT.setPosition(new Position(x * renT.getTileSize(), y * renT.getTileSize()));
                toAdd.addToPlayArea(renT);
            }
        }
    }
    
    public int getTileSize()
    {
        return tileSize;
    }
}
