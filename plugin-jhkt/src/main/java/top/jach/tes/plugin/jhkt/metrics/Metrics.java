package top.jach.tes.plugin.jhkt.metrics;

import lombok.Data;
import lombok.extern.java.Log;
import top.jach.tes.plugin.tes.code.go.GoFile;
import top.jach.tes.plugin.tes.code.go.GoPackage;

import java.util.*;

/**
 * @author: qiming
 * @date: 2020/1/11 16:01
 * @description:
 */
@Data
@Log
public class Metrics {
    //微服务名称
    private String elementName;
    /**
     * 大小
     */

    //微服务的包的个数
    private int packageCout;
    //微服务的文件总数
    private Long fileCount;
    //微服务订阅的接口数量
    private int subTopicsCount;
    //微服务发布的接口数量
    private int pubTopicsCount;
    //微服务中去除独立的包的包的总数
    private int dewightPackageCout;
    //独立的包的个数
    private int independencePackageCout;

    /**
     * 内聚
     */
    //服务接口使用内聚性
    private double IUCohesion;
    //微服务订阅的接口数据类型相似度（近似，算法可能有问题）
    private double subTopicSimilarity;
    //微服务发布的接口数据类型相似度（近似，算法可能有问题）
    private double pubTopicSimilarity;
    //微服务连通块的个数
    private int connectingBlocks;



    /**
     * 耦合
     */
    //微服务间的扇入耦合
    private int fanInCoupling;
    //微服务间的扇出耦合
    private int fanOutCoupling;
    //微服务间的扇入耦合和扇出耦合两者之和
    private int fanInAndOutCoupling;
    //微服务间的扇入耦合和扇出耦合两者之积
    private long fanInMultiOutCoupling;
    //微服务间相互依赖耦合
    private int interdependenceCoupling;
    //微服务内的扇入/扇出耦合
    private int fanInOrOutCouplingInSameMicro;
    //微服务内的扇入扇出耦合的乘积
    private long fanInMultiOutCouplingInSameMicro;

    /**
     * 复杂度
     */
    //微服务包的独立等级
    private Double packageIndependenceLevel;
    //微服务包中的元素独立等级
    private Double fileIndependenceLevel;
    //微服务的传播成本
    private Double propagationCost;
    //微服务订阅的接口能够处理的数据结构数量，可以看作该微服务所产生的和功能直接相关的数据结构（近似，具体需要实际的接口信息，在这里认为每一个oneof是一个数据结构）
    private int subDataStructureCount;
    //微服务发布的接口能够处理的数据结构数量（近似，具体需要实际的接口信息，在这里认为每一个oneof是一个数据结构）
    private int pubDataStructureCount;
    //上述两者之和，即微服务和功能直接相关的数据结构数量
    private int allDataStructureCount;
    //微服务的连通路径
    private int pathNum;



    //暂时没用到
    private Double independenceCoupling;
    //暂时没用到
    private Double communicationCohesive;

    private int[][] initMatrix;

    /**
     * 单个微服务与多个package对应
     */
    private List<GoPackage> packagesInMicroList;
    /**
     * 单个微服务中一个包依赖哪些包(不限制同一个微服务中的包)
     */
    private Map<String, List<String>> packagesInPackageMap;

    /**
     * 单个微服务中的一个包中有哪些文件
     */
    private Map<String, List<GoFile>> filesInPackageMap;
    /**
     * 单个微服务中一个文件依赖哪些包
     */
    private Map<String,List<GoPackage>> packagesInFileMap;
    /**
     * 单个微服务中的一个包依赖自身微服务中的包
     */
    private Map<String, List<String>> packagesDependenceInSameMicro;
    private List<String> zeroOutDegreePackageName=new ArrayList<>();
    private List<String> zeroInDegreePackageName=new ArrayList<>();

    private Map<String,Integer> subTopicsOneOf;
    private Map<String,Integer> pubTopicsOneOf=new HashMap<>();

    //被本微服务中的其他包 依赖过的包
    private Map<String,Set<String>> dependPacksMap;
//    private Set<String> dependPackSet;

    Map<Integer,Map<String,List<String>>> connectedBlockPacksDependeceInSameMicro=new HashMap<>();
    /**
     * pubTopics、subTopics、内聚等级
     */
    //订阅的其他微服务的名称及其接口名称 (微服务名称，订阅的微服务中的接口名称)
    private Map<String,List<String>> subscribeMicroMap = new HashMap<>();
    //被其他微服务订阅 (被订阅的微服务名称，被订阅的接口名称)
    private Map<String,List<String>> publishMicroMap = new HashMap<>();



    @Override
    public String toString(){
//        printPacksInMicro(packagesInMicroList);
//        printPacksInPack(packagesInPackageMap);
//        printPacsInPackWithSameMicro();
//        printFilesInPack(filesInPackageMap);
//        printPacksInFile(packagesInFileMap);
//        printDependPacksSet();
//        printInitMatrix(initMatrix);
//        printsubscribeMicroMap();
//        printpublishMicroMap();
//        printPubtopicOneOfMap();

        return  "微服务名称为"+elementName+" ;微服务的路径是 "+pathNum +"; 包的独立等级为"+packageIndependenceLevel+"; 文件总个数为"+fileCount+"; 文件的独立等级为"+fileIndependenceLevel
                +"; 传播成本为"+propagationCost+"; pubTopics的个数为"+pubTopicsCount+"; subTopics的个数为"+subTopicsCount+"; topics的总数为"+(pubTopicsCount+subTopicsCount)+
                "；连通块的个数为"+connectingBlocks+"; 服务接口调用内聚性为："+IUCohesion+"; subDataStructureCount数量为："+subDataStructureCount
                +"；pubDataStructureCount的数量为："+pubDataStructureCount+"; 能够处理的数据结构的总量为："+allDataStructureCount+";发布接口的结构相似度为："+pubTopicSimilarity
                +"；订阅接口的结构相似度为："+subTopicSimilarity+"；微服务间的扇入耦合为"+fanInCoupling+"；微服务间的扇出耦合为："+fanOutCoupling+"；微服务间的扇入和扇出耦合之和为"+fanInAndOutCoupling
                +"；微服务间相互依赖耦合为"+interdependenceCoupling+"；微服务间的扇入和扇出耦合之积为："+fanInMultiOutCoupling+"；微服务内的扇入/扇出耦合为"+fanInOrOutCouplingInSameMicro
                +"；微服务内的扇入扇出乘积之和为："+fanInMultiOutCouplingInSameMicro+"；微服务包的个数是为："+packageCout+"   "+dewightPackageCout+"   "+ independencePackageCout;

//        return "";
    }

    public int calConnectingBlocks(){
        Map<String,Boolean> isVisited = new HashMap<>();
        for(GoPackage goPackage: packagesInMicroList){
            isVisited.put(goPackage.getPath(),false);
        }
        int connectingBlocks=0;
        for(Map.Entry<String,List<String>> entry: packagesDependenceInSameMicro.entrySet()){
            //改包不依赖任何包，则出度为0
            if(entry.getValue().size()==0){
                zeroOutDegreePackageName.add(entry.getKey());
            }
            //如果该包未被本微服务中的其他包依赖，那么入度为0
            if(!dependPacksMap.containsKey(entry.getKey())){
                zeroInDegreePackageName.add(entry.getKey());
            }
            //该零接表初始位置未被访问到且出度不为0
            if(!isVisited.get(entry.getKey()) && entry.getValue().size()>0){
                Queue<String> queue = new LinkedList<>();
                Map<String,List<String>> connectedBlocksPacksDepenMap= new HashMap<>();
                queue.add(entry.getKey());
                //将该位置标志为已访问
                isVisited.put(entry.getKey(),true);
                int nodeCount=0;
                while(!queue.isEmpty()){
                    String startPackageName=queue.poll();
                    nodeCount++;
                    //获取该包所依赖的包
                    List<String> connectedPackageNames=packagesDependenceInSameMicro.get(startPackageName);
                    connectedBlocksPacksDepenMap.put(startPackageName,connectedPackageNames);
                    //获取依赖本包 的包
                    Set<String> dependedPackagesNames=dependPacksMap.get(startPackageName);
                    List<String> needVisitedPackages = new ArrayList<>(connectedPackageNames);
                    if(dependedPackagesNames!=null && dependedPackagesNames.size()>0){
                        needVisitedPackages.addAll(dependedPackagesNames);
                    }

                    for(String s: needVisitedPackages){
                        if(!isVisited.get(s)){
                            queue.add(s);
                            isVisited.put(s,true);
                        }
                    }
                }
                connectingBlocks++;
                //System.out.println("连通块"+connectingBlocks+"的个数为"+nodeCount);
                connectedBlockPacksDependeceInSameMicro.put(connectingBlocks,connectedBlocksPacksDepenMap);
            }
        }
        //计算微服务内有多少孤立的包
        int disconnectNum=0;
        for(Map.Entry<String,Boolean> entry:isVisited.entrySet()){
            if(!entry.getValue()){
                disconnectNum++;
            }
        }
        this.independencePackageCout=disconnectNum;
        this.dewightPackageCout=this.packageCout-disconnectNum;
        //System.out.println(elementName+"中总计共有"+packagesInMicroList.size()+"个包，其中有"+disconnectNum+"个孤立的包");
//        System.out.println("入读为0的包共有"+zeroInDegreePackageName.size());
//        for(String s:zeroInDegreePackageName){
//            System.out.println(s);
//        }
//        System.out.println("出度为0的包共有"+zeroOutDegreePackageName.size());
//        for(String s: zeroOutDegreePackageName){
//            System.out.println(s);
//        }
        return connectingBlocks;
    }

    public void calPathNumDFS(Map<String,List<String>> packagesDependenceInSameMicro,String startPoint, List<String> zeroOutDegreePackageName,Map<String,Boolean> isVisited,List<String> tempPath) {
        if (zeroOutDegreePackageName.contains(startPoint)) {
            pathNum += 1;
//            System.out.println("路径依次是：");
//            tempPath.add(startPoint);
//            for (String s : tempPath) {
//                System.out.println(s);
//            }
//            tempPath.remove(tempPath.size()-1);
            return;
        }
        if (isVisited.get(startPoint)!=null && !isVisited.get(startPoint)) {
            isVisited.put(startPoint, true);
            tempPath.add(startPoint);
            List<String> paths = packagesDependenceInSameMicro.get(startPoint);
            for (String s : paths) {
                calPathNumDFS(packagesDependenceInSameMicro, s, zeroOutDegreePackageName, isVisited, tempPath);
            }
            isVisited.put(startPoint, false);
            tempPath.remove(tempPath.size() - 1);
        }else if(isVisited.get(startPoint)!=null && isVisited.get(startPoint)){
            //System.out.println(startPoint+"被重复访问，成环");
        }

    }

    public void calPathNum(){
        List<String> zeroInDegreePackageName=getZeroInDegreePackageName();
        List<String> zeroOutDegreePackageName=getZeroOutDegreePackageName();
//        Map<String,List<String>> packagesDependencyInSameMicro=getConnectedBlockPacksDependeceInSameMicro().get(1);
        Map<String,List<String>> packagesDependencyInSameMicro=getPackagesDependenceInSameMicro();
        Map<String,Boolean> isVisited=new HashMap<>();
        for(String pathName:packagesDependencyInSameMicro.keySet()){
            if(!isVisited.containsKey(pathName)){
                isVisited.put(pathName,false);
            }
        }
        boolean moreFlag=false;
        for(String zeroInDegree: zeroInDegreePackageName){
            if(packagesInMicroList.size()<100){
                moreFlag=true;
                calPathNumDFS(packagesDependenceInSameMicro,zeroInDegree,zeroOutDegreePackageName,isVisited,new ArrayList<>());
            }
        }
        if(!moreFlag){
            pathNum=3*getPackagesDependenceInSameMicro().size()*getPackagesDependenceInSameMicro().size()*getPackagesDependenceInSameMicro().size();
        }
//        System.out.println("路径数为"+pathNum);
    }

    public void calPackageDependenceLevel(){
        List<String> zeroInDegreePackages=getZeroInDegreePackageName();
        Map<String,Set<String>> dependPackagesMap=new HashMap<>();
        Map<String,Set<String>> dulipcateDependPacks=getDependPacksMap();
        //深复制
        for(Map.Entry<String,Set<String>> entry:dulipcateDependPacks.entrySet()){
            if(!dependPackagesMap.containsKey(entry.getKey())){
                dependPackagesMap.put(entry.getKey(),new HashSet<>());
            }
            dependPackagesMap.get(entry.getKey()).addAll(entry.getValue());
        }

        int packagesNodeCount=0;
        double packgesIndependenceSum=0;
        Queue<PackageNode> queue=new LinkedList<>();
        for (String s:zeroInDegreePackages){
            queue.add(new PackageNode(s,1));
        }
        while(!queue.isEmpty()){
            //计算当前层的个数
            int size=queue.size();
            for(int i=0;i<size;i++) {
                PackageNode node = queue.poll();
                for (Map.Entry<String, Set<String>> entry : dependPackagesMap.entrySet()) {
                    Set<String> packsEntrySet = entry.getValue();
                    if(!packsEntrySet.remove(node.getPackageName())){
                        continue;
                    }
                    if (packsEntrySet.size() == 0) {
                        PackageNode newNode = new PackageNode(entry.getKey(), node.getLayer() + 1);
                        newNode.dependencePackges.addAll(node.dependencePackges);
                        newNode.dependencePackges.add(node.packageName);
                        queue.add(newNode);
                    }
                }
                double singlePackageDependenceLevel = packagesNodeCount == 0 ?  1 :   1 - 1.0*node.dependencePackges.size() / packagesNodeCount;
                packgesIndependenceSum+=singlePackageDependenceLevel;
//                System.out.println("包名："+node.getPackageName()+"的独立等级为："+singlePackageDependenceLevel);
            }
            packagesNodeCount+=size;
        }
        double packageIndependenceLevel=1.0*packgesIndependenceSum/packagesNodeCount;
        this.setPackageIndependenceLevel(packageIndependenceLevel);
        System.out.println(getElementName()+"中进入队列的节点总个数为："+packagesNodeCount);
    }





    public void printDependPacksSet(){
//        System.out.println("本微服务中被依赖的包分别是：");
//        for(String s: dependPackSet){
//            System.out.println(s);
//        }
//        System.out.println("---------总数为："+dependPackSet.size());
        int num=0;
        for(Map.Entry<String,Set<String>> entry:dependPacksMap.entrySet()){
            if(elementName.equals("x_13/x_46f")){
                System.out.println("包"+entry.getKey()+"被"+entry.getValue().size()+"个包依赖（同一微服务中）");
                System.out.println("这些依赖的包是...共"+entry.getValue().size()+"个");
                for(String s : entry.getValue()){
                    System.out.println(s);
                }
            }
//            System.out.println(entry.getKey());
            num+=entry.getValue().size();
        }
//        System.out.println("另一种方法Map的总数为："+dependPacksMap.size());
        System.out.println(elementName+"中共有"+num+"对被依赖关系");
    }
    public void printPacksInMicro(List<GoPackage> list){
        System.out.println(elementName+"微服务中共有"+list.size()+"个包");
//        Set<String> packagesNames=new HashSet<>();
//        for(GoPackage goPackage:list){
//            packagesNames.add(goPackage.getPath());
//        }
//        System.out.println(elementName+"微服务中共有"+packagesNames.size()+"个包，set版本");
//        for(GoPackage goPackage:list){
//            System.out.println(goPackage.getPath());
//        }
//        System.out.println("---------");
    }
    public void printPacksInPack(Map<String,Set<GoPackage>> packagesInPackageMap){
        System.out.println(elementName+"微服务中的包共有"+packagesInPackageMap.size()+"组包依赖关系（包含不同微服务）");
        for(Map.Entry<String,Set<GoPackage>> entry: packagesInPackageMap.entrySet()){
            System.out.println("包"+entry.getKey()+"依赖"+entry.getValue().size()+"个包");
        }
        System.out.println("---------");
    }
    public void printFilesInPack(Map<String,List<GoFile>> filesInPackageMap){
        System.out.println(elementName+"微服务中的包共有"+filesInPackageMap.size()+"组包含文件");
        for(Map.Entry<String,List<GoFile>> entry:filesInPackageMap.entrySet()){
            System.out.println("包"+entry.getKey()+"中包含"+entry.getValue().size()+"个文件");
        }
        System.out.println("---------");
    }
    public void printPacksInFile(Map<String,List<GoPackage>> packagesInFileMap){
        System.out.println(elementName+"微服务中共有"+packagesInFileMap.size()+"个文件");
        for(Map.Entry<String,List<GoPackage>> entry: packagesInFileMap.entrySet()){
            System.out.println("文件"+entry.getKey()+"中依赖"+entry.getValue().size()+"个包");
        }
        System.out.println("---------");
    }
    public void printPacsInPackWithSameMicro(){
        System.out.println(elementName+"微服务中共有"+packagesDependenceInSameMicro.size()+"个包有依赖关系（只在同一微服务中）");
        int num=0;

        for(Map.Entry<String,List<String>> entry:packagesDependenceInSameMicro.entrySet()){
            if(elementName.equals("x_13/x_46f")){
                System.out.println("包"+entry.getKey()+"依赖"+entry.getValue().size()+"个包（同一微服务中）");
                System.out.println("这些被依赖的包是...共"+entry.getValue().size()+"个");
                for(String s : entry.getValue()){
                    System.out.println(s);
                }
            }
            num+=entry.getValue().size();
        }
        System.out.println("共有"+num+"对依赖关系");
//        System.out.println("---------");
    }

    public void printInitMatrix(int[][] initMatrix){
        System.out.println(elementName+"的初始状态矩阵为：");
        for(int i=0;i<initMatrix.length;i++){
            for(int j=0;j<initMatrix[0].length;j++){
                System.out.print(initMatrix[i][j]+"   ");
            }
            System.out.println();

        }
    }

    public void printsubscribeMicroMap(){
        System.out.println("微服务名称："+elementName+";它订阅其他的微服务信息如下：");
        for(Map.Entry<String,List<String>> subNames: subscribeMicroMap.entrySet()){
            System.out.println("订阅了"+subNames.getKey()+"的"+subNames.getValue().size()+"个接口");
        }
    }

    public void printpublishMicroMap(){
        System.out.println("微服务名称："+elementName+";它发布的接口被其他微服务订阅的情况如下：");
        for(Map.Entry<String,List<String>> pubNames: publishMicroMap.entrySet()){
            System.out.println("被"+pubNames.getKey()+"订阅了"+pubNames.getValue().size()+"个接口");
        }
    }

    public void printPubtopicOneOfMap(){
        System.out.println("微服务名称："+elementName+"，它的publishTopicOneOf如下：");
        for(Map.Entry<String,Integer> entry: pubTopicsOneOf.entrySet()){
            System.out.println(entry.getKey()+"的个数为："+entry.getValue());
        }
    }

    class PackageNode{
        private String packageName;
        private int layer;
        private int fileNums;
        Set<String> dependencePackges=new HashSet<>();

        public PackageNode(String packageName,int layer){
            this.packageName=packageName;
            this.layer=layer;
        }
        public String getPackageName(){
            return packageName;
        }
        public int getLayer(){
            return layer;
        }
        public int getFileNums(){
            return fileNums;
        }
    }

public static void main(String[] args){
        List<String> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add("a"+i);
        }
        Iterator<String> iterator = list.iterator();
        while(iterator.hasNext()){
            String s=iterator.next();
            boolean flag=false;
            if(s.equals("a2") || s.equals("a3")||s.equals("a4")){
                flag=true;
            }
            if(flag){
                iterator.remove();
            }
        }
        System.out.println(list.size());
        list.clear();
        for(String s:list){
            System.out.println("1");
        }
        Map<String,Set<String>> setMap=new HashMap<>();
        if(!setMap.containsKey("x_3.x_5.x_7/x_9/x_b/x_d/x_13/x_46f/x_6b/x_ab/x_6d/x_106f/x_fd7/x_80f7/x_1081")){
            setMap.put("x_3.x_5.x_7/x_9/x_b/x_d/x_13/x_46f/x_6b/x_ab/x_6d/x_106f/x_fd7/x_80f7/x_1081",new HashSet<>());
        }
        setMap.get("x_3.x_5.x_7/x_9/x_b/x_d/x_13/x_46f/x_6b/x_ab/x_6d/x_106f/x_fd7/x_80f7/x_1081").add("x_3.x_5.x_7/x_9/x_b/x_d/x_13/x_46f/x_6b/x_6d/c_demo/x_7fe9");
}



}
