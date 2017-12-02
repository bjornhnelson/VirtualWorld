public class DynamicVisitor extends AllFalseEntityVisitor {
    public Boolean visit(Ore a) { return true; }
    public Boolean visit(Vein b) { return true; }
    public Boolean visit(OreBlob c) { return true; }
    public Boolean visit(MinerNotFull d) { return true; }
    public Boolean visit(MinerFull e) { return true; }
    public Boolean visit(Quake f) { return true; }
    public Boolean visit(Fire g) { return true; }
}
