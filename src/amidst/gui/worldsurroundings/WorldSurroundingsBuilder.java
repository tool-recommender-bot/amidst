package amidst.gui.worldsurroundings;

import java.util.List;

import amidst.Options;
import amidst.fragment.FragmentGraph;
import amidst.fragment.FragmentManager;
import amidst.fragment.drawer.FragmentDrawer;
import amidst.fragment.layer.LayerBuilder;
import amidst.fragment.layer.LayerDeclaration;
import amidst.fragment.layer.LayerLoader;
import amidst.fragment.layer.LayerReloader;
import amidst.gui.widget.WidgetBuilder;
import amidst.minecraft.world.World;

public class WorldSurroundingsBuilder {
	private final Zoom zoom;
	private final BiomeSelection biomeSelection = new BiomeSelection();

	private final Options options;
	private final LayerBuilder layerBuilder;
	private final FragmentManager fragmentManager;

	public WorldSurroundingsBuilder(Options options, LayerBuilder layerBuilder) {
		this.options = options;
		this.zoom = new Zoom(options.maxZoom);
		this.layerBuilder = layerBuilder;
		this.fragmentManager = new FragmentManager(
				layerBuilder.getConstructors(),
				layerBuilder.getNumberOfLayers());
	}

	public WorldSurroundings create(World world) {
		Movement movement = new Movement(options.mapFlicking);
		WorldIconSelection worldIconSelection = new WorldIconSelection();
		List<LayerDeclaration> declarations = layerBuilder.getDeclarations();
		FragmentGraph graph = new FragmentGraph(declarations, fragmentManager);
		Map map = new Map(zoom, graph);
		LayerLoader layerLoader = layerBuilder.createLayerLoader(world,
				biomeSelection, options);
		fragmentManager.setLayerLoader(layerLoader);
		Iterable<FragmentDrawer> drawers = layerBuilder.createDrawers(zoom,
				worldIconSelection);
		Drawer drawer = new Drawer(map, movement, zoom, graph, drawers);
		LayerReloader layerReloader = layerBuilder
				.createLayerReloader(fragmentManager);
		WidgetBuilder widgetBuilder = new WidgetBuilder(world, map,
				biomeSelection, worldIconSelection, layerReloader, graph, zoom,
				fragmentManager, options);
		MapViewer mapViewer = new MapViewer(movement, zoom, world, map, drawer,
				worldIconSelection, layerReloader, widgetBuilder.create());
		return new WorldSurroundings(map, mapViewer, layerReloader, graph,
				zoom, fragmentManager);
	}
}
