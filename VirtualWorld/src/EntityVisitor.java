interface EntityVisitor<R> {

    R visit(Blacksmith blacksmith);
    R visit(MinerFull minerfull);
    R visit(MinerNotFull minernotfull);
    R visit(Obstacle obstacle);
    R visit(Ore ore);
    R visit(OreBlob oreblob);
    R visit(Quake quake);
    R visit(Vein vein);

}