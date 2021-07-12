import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class NodeDiversity {


    private HashMap<String, String> hmDisMeSHCodes = new HashMap<String, String>();
    private HashMap<String, String> hmDisMeSHTreeCodes = new HashMap<String, String>(); // for used by loadDisCat2HashMap function and stores the MeSH tree codes.

    /*
     * This function is used to calculate the bridge coefficient of node. This measure
     * is from the article of Woochang Hwang,  Aidong Zhang, et al. Bridgeing Centrality: graph mining from element level to group level. kdd 2008.
     * 2011.6.21 implemented by xuezhong zhou at Barabasi lab.
     */
    public double getBridgeCoefficient(String strFile, String strWFile) {

        double dblResult = 0.;
        int intMax = 0;

        HashMap<String, String> hsm = new HashMap<String, String>();
        HashMap<String, String> hsm2 = new HashMap<String, String>();

        try {

            BufferedReader bf = new BufferedReader(new FileReader(strFile));
            String strLine = null;
            BufferedWriter bfw = new BufferedWriter(new FileWriter(strWFile, true));
            while ((strLine = bf.readLine()) != null) {

                String[] strArr = strLine.split("\t");
                int intTmp = Integer.valueOf(strArr[0]).intValue();
                if (intMax < intTmp) intMax = intTmp;
                if (hsm.containsKey(strArr[0]) == false) {
                    hsm.put(strArr[0], strArr[1]);
                }
                if (hsm2.containsKey(strArr[0]) == false) {
                    hsm2.put(strArr[0], strArr[2]);
                }
            }
            bf.close();

            int[] intNodes = new int[intMax + 1];

            BufferedReader bf2 = new BufferedReader(new FileReader(strFile));

            while ((strLine = bf2.readLine()) != null) {

                String[] strArr2 = strLine.split("\t");
                String[] strIDs = strArr2[2].split(",");
                //初始化ID数组。
                for (int i = 0; i <= intMax; i++) {
                    intNodes[i] = 0;
                }
                for (int i = 0; i < strIDs.length; i++) {
                    intNodes[Integer.valueOf(strIDs[i]).intValue()] = 1;
                }
                //初始化完成。

                int intDegree = Integer.valueOf(strArr2[3]).intValue();

                //计算bridging coefficient
                double dblTmp = 0.;
                int intTmp = 0;

                for (int j = 0; j < strIDs.length; j++) {

                    String strIDsTmp = hsm2.get(strIDs[j]);
                    //System.out.println(strIDs[j]);
                    String[] strNbIDs = strIDsTmp.split(",");
                    intTmp = 0;

                    for (int k = 0; k < strNbIDs.length; k++) {

                        int intIndex = Integer.valueOf(strNbIDs[k]).intValue();
                        if (intNodes[intIndex] == 1) intTmp++;

                    }
                    if (strNbIDs.length == 1) {
                        //对除0情况进行处理。
                        dblTmp += 0.0;
                    } else {
                        dblTmp += (0. + strNbIDs.length - intTmp - 1) / (strNbIDs.length - 1);
                    }

                }

                dblTmp = dblTmp / intDegree;

                bfw.write(strArr2[0] + "\t" + strArr2[1] + "\t" + dblTmp + "\n");
                System.out.println(strArr2[0] + "\t" + strArr2[1] + "\t" + dblTmp);
            }
            bfw.close();
            bf2.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dblResult;
    }

    /**
     * This function is used to calculate the bridge coefficient of node. This measure
     * is from the article of Woochang Hwang,  Aidong Zhang, et al. Bridgeing Centrality: graph mining from element level to group level. kdd 2008.
     * 2011.6.21 implemented by xuezhong zhou at Barabasi lab.
     * 根据特殊情况做了计算公式修改。
     */
    public void getBridgeCoefficient_m(String strFile, String strWFile, boolean blDiversity) {

        //double dblResult=0.;
        int intMax = 0;

        HashMap<String, String> hsm = new HashMap<String, String>();
        HashMap<String, String> hsm2 = new HashMap<String, String>();

        try {

            BufferedReader bf = new BufferedReader(new FileReader(strFile));
            String strLine = null;
            BufferedWriter bfw = new BufferedWriter(new FileWriter(strWFile, true));
            while ((strLine = bf.readLine()) != null) {

                String[] strArr = strLine.split("\t");
                int intTmp = Integer.valueOf(strArr[0]).intValue();
                if (intMax < intTmp) intMax = intTmp;
                if (hsm.containsKey(strArr[0]) == false) {
                    hsm.put(strArr[0], strArr[1]);
                }
                if (hsm2.containsKey(strArr[0]) == false) {
                    hsm2.put(strArr[0], strArr[2]);
                }
            }
            bf.close();

            int[] intNodes = new int[intMax + 1];

            BufferedReader bf2 = new BufferedReader(new FileReader(strFile));

            while ((strLine = bf2.readLine()) != null) {

                String[] strArr2 = strLine.split("\t");
                String[] strIDs = strArr2[2].split(",");
                //初始化ID数组。
                for (int i = 0; i <= intMax; i++) {
                    intNodes[i] = 0;
                }
                for (int i = 0; i < strIDs.length; i++) {
                    intNodes[Integer.valueOf(strIDs[i]).intValue()] = 1;
                }
                //初始化完成。

                int intDegree = Integer.valueOf(strArr2[3]).intValue();

                //计算bridging coefficient
                double dblTmp = 0.;
                int intTmp = 0;

                for (int j = 0; j < strIDs.length; j++) {

                    String strIDsTmp = hsm2.get(strIDs[j]);
                    //System.out.println(strIDs[j]);
                    String[] strNbIDs = strIDsTmp.split(",");
                    intTmp = 0;

                    for (int k = 0; k < strNbIDs.length; k++) {

                        int intIndex = Integer.valueOf(strNbIDs[k]).intValue();
                        if (intNodes[intIndex] == 1) intTmp++;

                    }
                    if (strNbIDs.length == 1) {
                        //对除0情况进行处理。
                        dblTmp += 0.0;
                    } else {
                        dblTmp += (0. + strNbIDs.length - intTmp - 1) / (strNbIDs.length - 1);
                    }

                }
                if (blDiversity == false) {
                    //计算node bridging coefficient，否则计算node diversity.
                    dblTmp = dblTmp / intDegree;
                }
                bfw.write(strArr2[0] + "\t" + strArr2[1] + "\t" + dblTmp + "\n");
                System.out.println(strArr2[0] + "\t" + strArr2[1] + "\t" + dblTmp);
            }
            bfw.close();
            bf2.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //return dblResult;
    }

    /*
     * This function is used to calculate the bridge coefficient of edges. This measure
     * is from the article of Woochang Hwang,  Aidong Zhang, et al. Bridgeing Centrality: graph mining from element level to group level. kdd 2008.
     * 2011.6.21 implemented by xuezhong zhou at Barabasi lab.
     * @param strFile 带有节点度信息的数据文件；
     * @param strNodeCoefficientFile 具有节点bridging coefficient信息的数据文件
     * @param strWFile 最终的数据文件。
     */

    public void caculateEdgeBridgingCoefficient(String strFile, String strNodeCoefficientFile,
                                                String strWFile, boolean blEdgeDiversity) {

        int intMax = 0;

        HashMap<String, String> hsm = new HashMap<String, String>();
        HashMap<String, String> hsm2 = new HashMap<String, String>(); //包含节点及其邻接节点信息的哈希表。

        try {

            BufferedReader bf = new BufferedReader(new FileReader(strFile));
            String strLine = null;
            System.out.println("--------------Start:Reading data to HashMap!---------" + new Date());
            while ((strLine = bf.readLine()) != null) {

                String[] strArr = strLine.split("\t");
                int intTmp = Integer.valueOf(strArr[0]).intValue();
                if (intMax < intTmp) intMax = intTmp;
                if (hsm.containsKey(strArr[0]) == false) {
                    hsm.put(strArr[0], strArr[1]);
                }
                if (hsm2.containsKey(strArr[0]) == false) {
                    hsm2.put(strArr[0], strArr[2]);
                }
            }
            bf.close();
            System.out.println("--------------End:Read data to HashMap!---------" + new Date());
            System.out.println("--------------Start:Reading Node BC data to Array!---------" + new Date());
            int[] intNodes = new int[intMax + 1];
            double[] dblNodeBC = new double[intMax + 1];
            for (int i = 0; i <= intMax; i++) {
                dblNodeBC[i] = 0.;
            }
            BufferedReader bfNodeBC = new BufferedReader(new FileReader(strNodeCoefficientFile));
            String strLineBC = null;
            //初始化node bridge coefficient 数组。
            while ((strLineBC = bfNodeBC.readLine()) != null) {

                String[] strBCArr = strLineBC.split("\t");
                if (strBCArr[2].equalsIgnoreCase("Infinity")) strBCArr[2] = "0.0"; //对无穷数值进行特殊处理。
                dblNodeBC[Integer.valueOf(strBCArr[0]).intValue()] = Double.valueOf(strBCArr[2]).doubleValue();

            }
            bfNodeBC.close();
            System.out.println("--------------End:Read Node BC data to Array!---------" + new Date());
            BufferedWriter bfw = new BufferedWriter(new FileWriter(strWFile, true));
            BufferedReader bf2 = new BufferedReader(new FileReader(strFile));
            System.out.println("--------------Start:Computing Edge BC!---------" + new Date());
            while ((strLine = bf2.readLine()) != null) {

                String[] strArr2 = strLine.split("\t");
                String[] strIDs = strArr2[2].split(",");
                int intNodeNo = Integer.valueOf(strArr2[0]).intValue(); //记录节点编号。
                //初始化ID数组。
                for (int i = 0; i <= intMax; i++) {
                    intNodes[i] = 0;
                }
                for (int i = 0; i < strIDs.length; i++) {
                    intNodes[Integer.valueOf(strIDs[i]).intValue()] = 1;
                }
                //初始化完成。

                int intDi = Integer.valueOf(strArr2[3]).intValue();

                //计算bridging coefficient
                double dblTmp = 0.;
                int intTmp = 0;

                for (int j = 0; j < strIDs.length; j++) {

                    String strIDsTmp = hsm2.get(strIDs[j]);
                    //System.out.println(strIDs[j]);
                    String[] strNbIDs = strIDsTmp.split(",");
                    int intDj = strNbIDs.length;
                    intTmp = 0;

                    for (int k = 0; k < strNbIDs.length; k++) {

                        int intIndex = Integer.valueOf(strNbIDs[k]).intValue();
                        if (intNodes[intIndex] == 1) intTmp++;

                    }
                    //dblTmp=(d(i)nd(i)+d(j)nd(j))/(d(i)+d(j))(intTmp+1)
                    if (blEdgeDiversity == false) {
                        dblTmp = (intDi * dblNodeBC[intNodeNo] + intDj * dblNodeBC[Integer.valueOf(strIDs[j]).intValue()]) / ((intDi + intDj) * (intTmp + 1));
                    } else {
                        //需要针对getBridgeCoefficient_m产生的node diversity数值。

                        dblTmp = Math.min(intDi, intDj) * (dblNodeBC[intNodeNo] + dblNodeBC[Integer.valueOf(strIDs[j]).intValue()]) / (2 * (intTmp + 1));
                    }
                    if (Integer.valueOf(strArr2[0]).intValue() < Integer.valueOf(strIDs[j]).intValue()) {

                        //仅输出上三角边。

                        bfw.write(strArr2[0] + "\t" + strArr2[1] + "\t" + strIDs[j] + "\t" + hsm.get(strIDs[j]) + "\t" + dblTmp + "\n");
                        System.out.println(strArr2[0] + "\t" + strArr2[1] + "\t" + strIDs[j] + "\t" + hsm.get(strIDs[j]) + "\t" + dblTmp);
                    }


                }

            }
            bfw.close();
            bf2.close();
            System.out.println("--------------End:Computing Edge BC!---------" + new Date());

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param strFile    the edge file with the format of node_name\tnode_name\n
     * @param strFileObj the result degree file with the format of nodeid\tnode_name\tnodeid list\tdegree\n.
     * @param i          the index of node_name 1
     * @param j          the index of node_name 2
     * @author zxz 2011.10.12
     * Generate the degree number of nodes and the related neighbor node list.
     */

    public void generateNodeDegree(String strFile, String strFileObj, String strSplit, int i, int j) {

        int intID = 1; //记录节点序号。
        int intDegree = 0;
        String strOldNode = "";

        HashMap<String, Integer> hsm = new HashMap<String, Integer>(); //存储节点名称以及序号。
        HashMap<String, String> hsm2 = new HashMap<String, String>(); //存储节点名称以及对应的邻接节点列表。
        String strIDs = "";

        String strID1, strID2;

        //String strNode1, strNode2;
        try {


            BufferedReader bf = new BufferedReader(new FileReader(strFile));

            BufferedWriter bfw = new BufferedWriter(new FileWriter(strFileObj, true));

            String strLine = null;

            while ((strLine = bf.readLine()) != null) {

                String[] strArr = strLine.split(strSplit);
                System.out.println(strSplit);
                if (strArr[i].equalsIgnoreCase(strArr[j]) == false) {
                    //去除自环。
                    if (hsm.containsKey(strArr[i]) == false) {

                        hsm.put(strArr[i], Integer.valueOf(intID++));

                    }
                    if (hsm.containsKey(strArr[j]) == false) {
                        hsm.put(strArr[j], Integer.valueOf(intID++));
                    }
                    if (hsm2.containsKey(strArr[i]) == false) {
                        hsm2.put(strArr[i], String.valueOf((Integer) hsm.get(strArr[j]).intValue()));
                    } else {
                        String strTmp = (String) hsm2.get(strArr[i]);
                        String strIDTmp = String.valueOf((Integer) hsm.get(strArr[j]).intValue());

                        if (("," + strTmp + ",").indexOf("," + strIDTmp + ",") < 0) {
                            hsm2.remove(strArr[i]);
                            hsm2.put(strArr[i], strTmp + "," + strIDTmp);
                        }

                    }

                    if (hsm2.containsKey(strArr[j]) == false) {

                        hsm2.put(strArr[j], String.valueOf((Integer) hsm.get(strArr[i]).intValue()));

                    } else {

                        String strTmp = (String) hsm2.get(strArr[j]);

                        String strIDTmp = String.valueOf((Integer) hsm.get(strArr[i]).intValue());

                        if (("," + strTmp + ",").indexOf("," + strIDTmp + ",") < 0) {

                            hsm2.remove(strArr[j]);
                            hsm2.put(strArr[j], strTmp + "," + strIDTmp);
                        }

                    }

                }

            }
            if (!hsm2.isEmpty()) {

                Iterator it = hsm2.entrySet().iterator();

                while (it.hasNext()) {

                    Map.Entry me = (Map.Entry) it.next();
                    String strKey = (String) me.getKey();
                    String strVal = (String) me.getValue();
                    String[] strTmpArr = strVal.split(",");
                    System.out.println("----------Write one line:" + strKey + "----------");
                    bfw.write(hsm.get(strKey) + "\t" + strKey + "\t" + strVal + "\t" + strTmpArr.length + "\n");
                }

            }

            hsm.clear();
            hsm2.clear();
            bf.close();
            bfw.close();
            System.out.println("----------Finished!----------");

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 本函数用于处理Node Diversity和Edge Bridging Diversity结果文件的代码名称化处理
     *
     * @param strFileND      结果文件如pubgene_0.999_sympbased_nd_bdgc.txt
     * @param strFileCodeND  node diversity 代码文件如dis_cm_code_COH.txt
     * @param strDisCode     名称与代码文件如disease_id_name.txt
     * @param strFileNDWrite 结果文件名称
     * @param IndexArr       表示所要处理的文件中哪些列需要进行名称处理。
     */
    public void getTermNamesUsingCode(String strFileND, String strFileCodeND, String strDisCode, String strFileNDWrite, int[] IndexArr, int intCodeNo) {

        BufferedReader bf, bfNDCode, bfDisCode;
        BufferedWriter bfw;
        int intCodeNum = intCodeNo;

        HashMap<String, String> hmNames = new HashMap<String, String>();

        String[] strCodeArr = new String[intCodeNum];

        try {


            bfNDCode = new BufferedReader(new FileReader(strFileCodeND));
            String strLine = null;

            while ((strLine = bfNDCode.readLine()) != null) {

                String[] strArr = strLine.split("\t");
                strCodeArr[Integer.valueOf(strArr[0])] = strArr[1];

            }
            bfNDCode.close();
            bfDisCode = new BufferedReader(new FileReader(strDisCode));

            while ((strLine = bfDisCode.readLine()) != null) {

                String[] strArr = strLine.split("\t");

                if (!hmNames.containsKey(strArr[0])) {

                    hmNames.put(strArr[0], strArr[1]);

                }
            }
            bfDisCode.close();

            bf = new BufferedReader(new FileReader(strFileND));
            bfw = new BufferedWriter(new FileWriter(strFileNDWrite, true));

            while ((strLine = bf.readLine()) != null) {

                String[] strArr = strLine.split("\t");
                String strTmp = "";
                for (int i = 0; i < strArr.length; i++) {
                    for (int j = 0; j < IndexArr.length; j++) {
                        if (i == IndexArr[j]) {
                            strArr[i] = hmNames.get(String.valueOf(strCodeArr[Integer.valueOf(strArr[i]).intValue()]));
                        }
                    }
                    strTmp += strArr[i] + "\t";
                }
                strTmp = strTmp.substring(0, strTmp.length() - 1);

                bfw.write(strTmp + "\n");
                System.out.println(strTmp);
            }
            bf.close();
            bfw.close();


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @param strNeighorFile      具有节点邻接结构的文件，读取generateNodeDegree函数产生的结果文件。
     * @param strNodeName         指定的节点名称，不是节点序号。
     * @param strNeighorWriteFile 结果文件。
     * @param intNodeCode         对应节点的code.
     * @param intProcType         1: regular;2: no self node;3: no one degree node.
     */

    public void generateNodeNeighorNetwork(String strNeighorFile, String strNodeName, String intNodeCode,
                                           String strNeighorWriteFile, int intProcType) {

        BufferedWriter bfw;
        BufferedReader bf1, bf2;
        String[] strNeighborArr = null;

        HashMap<String, String> hmNodes = new HashMap<String, String>();
        HashMap<String, Integer> hmNodedegree = new HashMap<String, Integer>();

        try {

            bf1 = new BufferedReader(new FileReader(strNeighorFile));

            String strLine = null;

            while ((strLine = bf1.readLine()) != null) {

                String[] strArr = strLine.split("\t");
                if (strArr[1].equalsIgnoreCase(strNodeName)) {
                    strNeighborArr = strArr[2].split(",");
                }
                hmNodes.put(strArr[0], strArr[1]);
            }

            bf1.close();

            bf2 = new BufferedReader(new FileReader(strNeighorFile));

            String strResultFile = strNeighorWriteFile;

            if (intProcType == 3) {
                strResultFile += "1";
            }

            bfw = new BufferedWriter(new FileWriter(strResultFile, true));

            int intRecNo = 0;

            while ((strLine = bf2.readLine()) != null) {

                String[] strArr2 = strLine.split("\t");

                int intNgbNo = strNeighborArr.length;

                for (int i = 0; i < intNgbNo; i++) {

                    if (strArr2[0].equalsIgnoreCase(strNeighborArr[i])

                            && (!strArr2[1].equalsIgnoreCase(strNodeName))) {

                        System.out.println("One neighbor node:" + intRecNo++);

                        if (intProcType == 1) {

                            bfw.write(strNodeName + "\t" + strArr2[1] + "\n");

                        } else {

                            if (intProcType == 3) {

                                bfw.write(strNodeName + "\t" + strArr2[1] + "\n");

                                if (!hmNodedegree.containsKey(strNodeName)) {

                                    hmNodedegree.put(strNodeName, Integer.valueOf(1));

                                } else {

                                    int intTmp = hmNodedegree.get(strNodeName).intValue() + 1;
                                    hmNodedegree.remove(strNodeName);
                                    hmNodedegree.put(strNodeName, Integer.valueOf(intTmp));
                                }
                                if (!hmNodedegree.containsKey(strArr2[1])) {

                                    hmNodedegree.put(strArr2[1], Integer.valueOf(1));

                                } else {

                                    int intTmp = hmNodedegree.get(strArr2[1]).intValue() + 1;
                                    hmNodedegree.remove(strArr2[1]);
                                    hmNodedegree.put(strArr2[1], Integer.valueOf(intTmp));
                                }
                            }
                        }


                        String[] strTmpArr = strArr2[2].split(",");

                        for (int j = 0; j < strTmpArr.length; j++) {

                            int k = 0;

                            for (; k < intNgbNo; k++) {

                                //去除重复的边输出。
                                if (strTmpArr[j].equalsIgnoreCase(strNeighborArr[k])) {
                                    if (Integer.valueOf(strTmpArr[j]).intValue() > Integer.valueOf(strArr2[0]).intValue()) {
                                        bfw.write(strArr2[1] + "\t" + hmNodes.get(strTmpArr[j]) + "\n");

                                        if (intProcType == 3) {
                                            String strTmp = hmNodes.get(strTmpArr[j]);

                                            if (!hmNodedegree.containsKey(strTmp)) {

                                                hmNodedegree.put(strTmp, Integer.valueOf(1));

                                            } else {

                                                int intTmp = hmNodedegree.get(strTmp).intValue() + 1;
                                                hmNodedegree.remove(strTmp);
                                                hmNodedegree.put(strTmp, Integer.valueOf(intTmp));
                                            }
                                            if (!hmNodedegree.containsKey(strArr2[1])) {

                                                hmNodedegree.put(strArr2[1], Integer.valueOf(1));

                                            } else {

                                                int intTmp = hmNodedegree.get(strArr2[1]).intValue() + 1;
                                                hmNodedegree.remove(strArr2[1]);
                                                hmNodedegree.put(strArr2[1], Integer.valueOf(intTmp));
                                            }
                                        }

                                    }
                                    break;
                                }

                            }

                            if (k == intNgbNo && !intNodeCode.equalsIgnoreCase(strTmpArr[j])) {

                                bfw.write(strArr2[1] + "\t" + hmNodes.get(strTmpArr[j]) + "\n");

                                if (intProcType == 3) {
                                    String strTmp = hmNodes.get(strTmpArr[j]);

                                    if (!hmNodedegree.containsKey(strTmp)) {

                                        hmNodedegree.put(strTmp, Integer.valueOf(1));

                                    } else {

                                        int intTmp = hmNodedegree.get(strTmp).intValue() + 1;
                                        hmNodedegree.remove(strTmp);
                                        hmNodedegree.put(strTmp, Integer.valueOf(intTmp));
                                    }
                                    if (!hmNodedegree.containsKey(strArr2[1])) {

                                        hmNodedegree.put(strArr2[1], Integer.valueOf(1));

                                    } else {

                                        int intTmp = hmNodedegree.get(strArr2[1]).intValue() + 1;
                                        hmNodedegree.remove(strArr2[1]);
                                        hmNodedegree.put(strArr2[1], Integer.valueOf(intTmp));
                                    }
                                }
                            }

                        }

                        break;
                    }
                }
            }

            bf2.close();

            bfw.close();

            hmNodes.clear();

            if (intProcType == 3) {

                bfw = new BufferedWriter(new FileWriter(strNeighorWriteFile, true));
                bf2 = new BufferedReader(new FileReader(strResultFile));

                while ((strLine = bf2.readLine()) != null) {

                    String[] strArr = strLine.split("\t");

                    if (hmNodedegree.get(strArr[0]).intValue() > 1 && hmNodedegree.get(strArr[1]).intValue() > 1) {
                        bfw.write(strLine + "\n");
                    }
                }

                bfw.close();
                bf2.close();
                File file = new File(strResultFile);
                if (file.delete()) {
                    System.out.println("成功删除临时文件：" + strResultFile + "!");
                }

            }
            hmNodedegree.clear();

            System.out.println("Finished!");

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @param strNeighorFile 具有节点邻接结构的文件，读取generateNodeDegree函数产生的结果文件。
     * @param strNodeName    指定的节点名称，不是节点序号。
     * @param intNodeCode    对应节点的code.
     * @author zxz 2011.12.20
     */

    public HashMap<String, Integer> generateNodeNeighorNetwork(String strNeighorFile,
                                                               String strNodeName, String intNodeCode) {

        BufferedWriter bfw;
        BufferedReader bf1, bf2;
        String[] strNeighborArr = null;

        HashMap<String, String> hmNodes = new HashMap<String, String>();
        HashMap<String, Integer> hmNodedegree = new HashMap<String, Integer>();
        HashMap<String, Integer> hmEdges = new HashMap<String, Integer>();

        try {

            bf1 = new BufferedReader(new FileReader(strNeighorFile));

            String strLine = null;

            while ((strLine = bf1.readLine()) != null) {

                String[] strArr = strLine.split("\t");
                if (strArr[1].equalsIgnoreCase(strNodeName)) {
                    strNeighborArr = strArr[2].split(",");
                }
                hmNodes.put(strArr[0], strArr[1]);
            }

            bf1.close();

            bf2 = new BufferedReader(new FileReader(strNeighorFile));

            int intRecNo = 0;

            int intEdgeNum = 0;

            while ((strLine = bf2.readLine()) != null) {

                String[] strArr2 = strLine.split("\t");

                int intNgbNo = strNeighborArr.length;

                for (int i = 0; i < intNgbNo; i++) {

                    if (strArr2[0].equalsIgnoreCase(strNeighborArr[i])

                            && (!strArr2[1].equalsIgnoreCase(strNodeName))) {

                        System.out.println("One neighbor node:" + intRecNo++);

                        hmEdges.put(strNodeName + "\t" + strArr2[1], intEdgeNum++);

                        String[] strTmpArr = strArr2[2].split(",");

                        for (int j = 0; j < strTmpArr.length; j++) {

                            int k = 0;

                            for (; k < intNgbNo; k++) {

                                //去除重复的边输出。
                                if (strTmpArr[j].equalsIgnoreCase(strNeighborArr[k])) {
                                    if (Integer.valueOf(strTmpArr[j]).intValue() > Integer.valueOf(strArr2[0]).intValue()) {

                                        hmEdges.put(strArr2[1] + "\t" + hmNodes.get(strTmpArr[j]), intEdgeNum++);

                                    }
                                    break;
                                }

                            }

                            if (k == intNgbNo && !intNodeCode.equalsIgnoreCase(strTmpArr[j])) {


                                hmEdges.put(strArr2[1] + "\t" + hmNodes.get(strTmpArr[j]), intEdgeNum++);
                            }

                        }
                        break;
                    }
                }
            }

            bf2.close();

            hmNodes.clear();

            hmNodedegree.clear();

            System.out.println("Finished!");


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hmEdges;

    }

    /**
     * @param strPPINetFile  the node degree ppi file with 5 integrated data sources: the node degree file of integrated_ppi_5_srcs_nofieldnames.txt.
     * @param strDisGeneFile the disease-gene file with 3 integrated data sources:dis_gene_3src_20111104.txt.
     * @author zxz 2011.12.20
     * This function is used to generate a disease related protein-protein neighbor network.
     * because one disease would have multiple related genes, we need combine the neighbor networks of genes
     * to one network.
     */

    public void getDisGeneNeighborNetWork(String strPPINetFile, String strDisGeneFile,
                                          String strRSFile, String strDisName, String strSplit) {

        BufferedWriter bfw;
        BufferedReader bf1;

        String strLine = null;
        String strTmp = "";

        int intEdgeNum = 0;

        String[] strArr = null;
        String[] strTmpArr = null;

        Iterator it = null;

        HashMap<String, String> hmGeneNodes = new HashMap<String, String>();
        HashMap<String, Integer> hmEdges = new HashMap<String, Integer>();
        HashMap<String, Integer> hmEdgeTmp = null;

        try {

            bf1 = new BufferedReader(new FileReader(strDisGeneFile));
            bfw = new BufferedWriter(new FileWriter(strRSFile, true));

            while ((strLine = bf1.readLine()) != null) {

                strArr = strLine.split(strSplit);

                if (strArr[2].equalsIgnoreCase(strDisName)) {

                    if (strTmp == "") strTmp = strArr[1];
                    else strTmp += strSplit + strArr[1];
                }
            }

            bf1.close();

            bf1 = new BufferedReader(new FileReader(strPPINetFile));

            while ((strLine = bf1.readLine()) != null) {

                strArr = strLine.split(strSplit);
                hmGeneNodes.put(strArr[1], strArr[0]);

            }

            bf1.close();

            if (strTmp != "") {

                strArr = strTmp.split(strSplit);

                for (int i = 0; i < strArr.length; i++) {

                    hmEdgeTmp = generateNodeNeighorNetwork(strPPINetFile, strArr[i],
                            hmGeneNodes.get(strArr[i]));

                    it = hmEdgeTmp.keySet().iterator();

                    while (it.hasNext()) {

                        strTmp = (String) it.next();
                        strTmpArr = strTmp.split(strSplit);

                        if (!hmEdges.containsKey(strTmp) &&
                                !hmEdges.containsKey(strTmpArr[1] + strSplit + strTmpArr[0])) {
                            hmEdges.put(strTmp, intEdgeNum++);

                            bfw.write(strTmp + "\n");

                            System.out.println(strTmp);
                        }
                    }

                }

            }

            bfw.close();

            hmEdges.clear();
            hmGeneNodes.clear();
            hmEdgeTmp.clear();

            System.out.println("Finished at the time:" + new Date());


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }

    /**
     * @param strGeneNDFile  the gene node diversity file.
     * @param strDisGeneFile the disease-gene association file.
     * @author zxz 2011.12.12
     */

    public void calculateDisAvgNodeDiversity(String strGeneNDFile, String strDisGeneFile,
                                             String strRSFile, String strSplit, int intIndex) {

        BufferedWriter bfw;
        BufferedReader bf1;

        String strLine = null;
        String strTmp = "";

        double dblTmp = 0.;

        String[] strArr = null;

        HashMap<String, String> hmDisNodes = new HashMap<String, String>();
        HashMap<String, Double> hmGeneNDs = new HashMap<String, Double>();

        try {

            bf1 = new BufferedReader(new FileReader(strDisGeneFile));
            bfw = new BufferedWriter(new FileWriter(strRSFile, true));
            bf1.readLine();

            while ((strLine = bf1.readLine()) != null) {

                strArr = strLine.split(strSplit);

                if (!hmDisNodes.containsKey(strArr[0])) {

                    hmDisNodes.put(strArr[0], strArr[1]);

                } else {
                    strTmp = hmDisNodes.get(strArr[0]);
                    hmDisNodes.remove(strArr[0]);
                    hmDisNodes.put(strArr[0], strTmp + strSplit + strArr[1]);
                }
            }

            bf1.close();

            bf1 = new BufferedReader(new FileReader(strGeneNDFile));

            while ((strLine = bf1.readLine()) != null) {

                strArr = strLine.split(strSplit);

                if (!hmGeneNDs.containsKey(strArr[intIndex])) {

                    hmGeneNDs.put(strArr[intIndex], Double.valueOf(strArr[intIndex + 1]));

                }
            }
            bf1.close();
            Iterator it = hmDisNodes.entrySet().iterator();
            Map.Entry<String, String> me = null;

            double dblCoef;
            int intNumTmp = 0;
            double dblSum = 0.;

            while (it.hasNext()) {

                me = (Map.Entry<String, String>) it.next();
                strArr = me.getValue().split(strSplit);

                for (int i = 0; i < strArr.length; i++) {

                    if (hmGeneNDs.containsKey(strArr[i])) {

                        dblCoef = hmGeneNDs.get(strArr[i]);
                        dblSum += dblCoef;
                        intNumTmp++;

                    }
                }

                bfw.write(me.getKey() + strSplit + (intNumTmp == 0 ? 0. : dblSum / intNumTmp) + "\n");
                System.out.println(me.getKey() + strSplit + (intNumTmp == 0 ? 0. : dblSum / intNumTmp) + "\n");

                dblSum = 0.;
                intNumTmp = 0;

            }

            bfw.close();

            System.out.println("Finished at the time:" + new Date());


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * @param strGeneNDFile  the gene node diversity file.
     * @param strDisGeneFile the disease-gene association file.
     * @author zxz 2011.12.19
     * This function is used to calculate the maximum disease related ppi node diversity.
     */

    public void calculateDisMaxNodeDiversity(String strGeneNDFile, String strDisGeneFile,
                                             String strRSFile, String strSplit, int intIndex) {

        BufferedWriter bfw;
        BufferedReader bf1;

        String strLine = null;
        String strTmp = "";

        double dblTmp = 0.;

        String[] strArr = null;

        HashMap<String, String> hmDisNodes = new HashMap<String, String>();
        HashMap<String, Double> hmGeneNDs = new HashMap<String, Double>();

        try {

            bf1 = new BufferedReader(new FileReader(strDisGeneFile));
            bfw = new BufferedWriter(new FileWriter(strRSFile, true));
            bf1.readLine();

            while ((strLine = bf1.readLine()) != null) {

                strArr = strLine.split(strSplit);

                if (!hmDisNodes.containsKey(strArr[0])) {

                    hmDisNodes.put(strArr[0], strArr[1]);

                } else {
                    strTmp = hmDisNodes.get(strArr[0]);
                    hmDisNodes.remove(strArr[0]);
                    hmDisNodes.put(strArr[0], strTmp + strSplit + strArr[1]);
                }
            }

            bf1.close();

            bf1 = new BufferedReader(new FileReader(strGeneNDFile));

            while ((strLine = bf1.readLine()) != null) {

                strArr = strLine.split(strSplit);

                if (!hmGeneNDs.containsKey(strArr[intIndex])) {

                    hmGeneNDs.put(strArr[intIndex], Double.valueOf(strArr[intIndex + 1]));

                }
            }
            bf1.close();
            Iterator it = hmDisNodes.entrySet().iterator();
            Map.Entry<String, String> me = null;

            double dblCoef = 0;

            while (it.hasNext()) {

                me = (Map.Entry<String, String>) it.next();
                strArr = me.getValue().split(strSplit);

                for (int i = 0; i < strArr.length; i++) {

                    if (hmGeneNDs.containsKey(strArr[i])) {

                        if (dblCoef < hmGeneNDs.get(strArr[i])) {

                            dblCoef = hmGeneNDs.get(strArr[i]);
                        }

                    }
                }

                bfw.write(me.getKey() + strSplit + dblCoef + "\n");
                System.out.println(me.getKey() + strSplit + dblCoef + "\n");
                dblCoef = 0;

            }

            bfw.close();

            System.out.println("Finished at the time:" + new Date());


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @author zxz 2011.12.12
     */

    public void disGeneNodeDiversity(String strDisNDFile, String strDisGAvgNDFile,
                                     String strRSFile, String strSplit, int intIndex) {

        BufferedWriter bfw;
        BufferedReader bf1;

        String strLine = null;
        String strTmp = "";

        double dblTmp = 0.;

        String[] strArr = null;

        HashMap<String, String> hmDisNodes = new HashMap<String, String>();
        HashMap<String, Double> hmGeneNDs = new HashMap<String, Double>();

        try {

            bf1 = new BufferedReader(new FileReader(strDisNDFile));

            bfw = new BufferedWriter(new FileWriter(strRSFile, true));

            while ((strLine = bf1.readLine()) != null) {

                strArr = strLine.split(strSplit);

                if (!hmDisNodes.containsKey(strArr[intIndex])) {

                    hmDisNodes.put(strArr[intIndex], strArr[intIndex + 1]);

                }
            }

            bf1.close();

            bf1 = new BufferedReader(new FileReader(strDisGAvgNDFile));

            while ((strLine = bf1.readLine()) != null) {

                strArr = strLine.split(strSplit);

                if (hmDisNodes.containsKey(strArr[0])) {

                    bfw.write(strLine + strSplit + hmDisNodes.get(strArr[0]) + "\n");

                }
            }

            bf1.close();
            bfw.close();

            System.out.println("Finished at the time:" + new Date());


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /**
     * @author zxz 2011.12.12
     * This function uses the neighbors of one disease to calculate the average node diversities.
     * This means that we also include the node diversity of the neighbors of one disease.
     */

    public void calculateDisNeighborAvgNodeDiversity(String strGeneNDFile, String strDisGeneFile,
                                                     String strDisNBFile, String strRSFile, String strSplit) {

        BufferedWriter bfw;
        BufferedReader bf1;

        String strLine = null;
        String strTmp = "";

        double dblTmp = 0.;

        String[] strArr = null;

        HashMap<String, String> hmDisNodes = new HashMap<String, String>();
        HashMap<String, String> hmDisNodes2 = new HashMap<String, String>();
        HashMap<String, Double> hmGeneNDs = new HashMap<String, Double>();
        HashMap<String, String> hmDisNBNodes = new HashMap<String, String>();
        HashMap<String, String> hmDisCodes = new HashMap<String, String>();
        HashMap<String, Integer> hmTmpGenes = new HashMap<String, Integer>();

        HashMap<String, Integer> hmNodeDegree = new HashMap<String, Integer>();

        try {

            bf1 = new BufferedReader(new FileReader(strDisGeneFile));
            bfw = new BufferedWriter(new FileWriter(strRSFile, true));

            bf1.readLine();

            System.out.println("Begin to get related data to memory " +
                    "at the time:" + new Date());

            while ((strLine = bf1.readLine()) != null) {

                strArr = strLine.split(strSplit);

                if (!hmDisNodes.containsKey(strArr[0])) {

                    hmDisNodes.put(strArr[0], strArr[1]);

                } else {

                    strTmp = hmDisNodes.get(strArr[0]);
                    hmDisNodes.remove(strArr[0]);
                    hmDisNodes.put(strArr[0], strTmp + strSplit + strArr[1]);
                }
            }

            bf1.close();

            bf1 = new BufferedReader(new FileReader(strGeneNDFile));

            while ((strLine = bf1.readLine()) != null) {

                strArr = strLine.split(strSplit);

                if (!hmGeneNDs.containsKey(strArr[1])) {

                    hmGeneNDs.put(strArr[1], Double.valueOf(strArr[2]));

                }
            }

            bf1.close();

            // read the disease neighbors file

            bf1 = new BufferedReader(new FileReader(strDisNBFile));

            while ((strLine = bf1.readLine()) != null) {

                strArr = strLine.split(strSplit);

                hmDisCodes.put(strArr[0], strArr[1]);
                hmDisNBNodes.put(strArr[1], strArr[2]);
                hmNodeDegree.put(strArr[1], Integer.valueOf(strArr[3]));

            }

            bf1.close();

            //Begin to get the disease neighbors related distinct genes.

            System.out.println("Begin to get related genes of diseases by expanding the disease neighbors " +
                    "at the time:" + new Date());

            Map.Entry<String, String> me = null;

            Iterator it1 = hmDisNBNodes.entrySet().iterator();

            String[] strTmpArr = null;

            while (it1.hasNext()) {

                me = (Map.Entry<String, String>) it1.next();

                strArr = me.getValue().split(",");

                hmTmpGenes.clear();

                strLine = "";

                for (int i = 0; i < strArr.length; i++) {

                    strTmp = hmDisNodes.get(hmDisCodes.get(strArr[i]));

                    strTmpArr = strTmp.split(strSplit);

                    for (int j = 0; j < strTmpArr.length; j++) {

                        if (!hmTmpGenes.containsKey(strTmpArr[j])) {

                            hmTmpGenes.put(strTmpArr[j], 1);

                            if (strLine.equalsIgnoreCase("")) {

                                strLine = strTmpArr[j];

                            } else {

                                strLine += strSplit + strTmpArr[j];

                            }

                        }
                    }

                }

                hmDisNodes2.put(me.getKey(), strLine);

            }

            hmDisNodes.clear();

            hmDisCodes.clear();

            hmDisNBNodes.clear();


            System.out.println("Finish " +
                    "at the time:" + new Date());

            Iterator it = hmDisNodes2.entrySet().iterator();

            double dblCoef;
            int intNumTmp = 0;
            double dblSum = 0.;

            while (it.hasNext()) {

                me = (Map.Entry<String, String>) it.next();

                strArr = me.getValue().split(strSplit);

                for (int i = 0; i < strArr.length; i++) {

                    if (hmGeneNDs.containsKey(strArr[i])) {

                        dblCoef = hmGeneNDs.get(strArr[i]);
                        dblSum += dblCoef;
                        intNumTmp++;

                    }
                }

                //intNumTmp*=hmNodeDegree.get(me.getKey());

                bfw.write(me.getKey() + strSplit + (intNumTmp == 0 ? 0. : dblSum / intNumTmp) + "\n");

                System.out.println(me.getKey() + strSplit + (intNumTmp == 0 ? 0. : dblSum / intNumTmp) + "\n");

                dblSum = 0.;

                intNumTmp = 0;

            }

            bfw.close();

            hmGeneNDs.clear();

            hmDisNodes2.clear();

            hmNodeDegree.clear();

            System.out.println("Finished at the time:" + new Date());


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @param strNDFile      the node diversity file generated by disGeneNodeDiversity.
     * @param dblWindow      designate the window size, such as 0.5,1.0.
     * @param intIndexWinVal the window size field index
     * @param intIndexAvg    the average value field index.
     * @param blZscore       true: calculate z-score before window size;false:using the original values.
     * @author zxz 2011.12.19
     */

    public void calculateWindowAvgVal(String strNDFile, String strSplit, String strRSFile,
                                      double dblWindow, int intIndexWinVal, int intIndexAvg, boolean blZscore) {

        BufferedWriter bfw;
        BufferedReader bf1;

        String strLine = null;
        String strTmp = "";

        double dblTmp = 0.;
        double dblMean1 = 0;
        double dblMean2 = 0;
        double dblSum1 = 0;
        double dblSum2 = 0;
        double dblStdEv1 = 0;
        double dblStdEv2 = 0;
        int intNodeNum = 0;
        Iterator it = null;

        Map.Entry<String, Double> me = null;

        String[] strArr = null;

        HashMap<String, Double> hmDisWinVals = new HashMap<String, Double>();
        HashMap<String, Double> hmDisAvgVals = new HashMap<String, Double>();
        HashMap<String, Double> hmDisWinZsVals = new HashMap<String, Double>();
        HashMap<String, Double> hmDisAvgZsVals = new HashMap<String, Double>();
        HashMap<String, String> hmDisWinZsFnlVals = new HashMap<String, String>();

        try {

            bf1 = new BufferedReader(new FileReader(strNDFile));
            bfw = new BufferedWriter(new FileWriter(strRSFile, true));

            System.out.println("Begin to get related data to memory " +
                    "at the time:" + new Date());

            while ((strLine = bf1.readLine()) != null) {

                strArr = strLine.split(strSplit);

                if (!hmDisWinVals.containsKey(strArr[intIndexWinVal])) {

                    hmDisWinVals.put(strArr[0], Double.valueOf(strArr[intIndexWinVal]));
                    dblSum1 += Double.valueOf(strArr[intIndexWinVal]);
                }
                if (!hmDisAvgVals.containsKey(strArr[intIndexAvg])) {

                    hmDisAvgVals.put(strArr[0], Double.valueOf(strArr[intIndexAvg]));
                    dblSum2 += Double.valueOf(strArr[intIndexAvg]);
                }
                intNodeNum++;
            }

            bf1.close();

            if (blZscore) {

                // need calculate the z-score of original values.
                // firstly calculate the standard deviation.

                double dblMin = 0;

                dblMean1 = dblSum1 / intNodeNum;

                it = hmDisAvgVals.entrySet().iterator();

                dblSum1 = 0;

                while (it.hasNext()) {

                    me = (Map.Entry<String, Double>) it.next();
                    dblSum1 += Math.pow(me.getValue() - dblMean1, 2);
                }

                dblStdEv1 = Math.sqrt(dblSum1 / (intNodeNum - 1));
                // calcualte the z-score

                it = hmDisAvgVals.entrySet().iterator();

                dblSum1 = 0;

                while (it.hasNext()) {

                    me = (Map.Entry<String, Double>) it.next();
                    hmDisAvgZsVals.put(me.getKey(), (me.getValue() - dblMean1) / dblStdEv1);

                }
                // calucate the next z-score values.

                dblMean2 = dblSum2 / intNodeNum;

                it = hmDisWinVals.entrySet().iterator();

                dblSum2 = 0;

                while (it.hasNext()) {

                    me = (Map.Entry<String, Double>) it.next();
                    dblSum2 += Math.pow(me.getValue() - dblMean2, 2);
                }

                dblStdEv2 = Math.sqrt(dblSum2 / (intNodeNum - 1));
                // calcualte the z-score

                it = hmDisWinVals.entrySet().iterator();

                dblSum1 = 0;

                while (it.hasNext()) {

                    me = (Map.Entry<String, Double>) it.next();
                    dblTmp = (me.getValue() - dblMean2) / dblStdEv2;

                    hmDisWinZsVals.put(me.getKey(), dblTmp);

                    if (dblMin > dblTmp) {
                        dblMin = dblTmp;
                    }

                }

                it = hmDisWinZsVals.entrySet().iterator();

                long lngWinTmp = 0;
                int intWinVal = 0;

                while (it.hasNext()) {

                    me = (Map.Entry<String, Double>) it.next();

                    dblTmp = me.getValue();

                    lngWinTmp = Math.round((dblTmp - Double.valueOf(dblMin).intValue()) / dblWindow);
                    intWinVal = Double.valueOf(Double.valueOf(dblMin).intValue() + lngWinTmp * dblWindow).intValue();
                    if (!hmDisWinZsFnlVals.containsKey(String.valueOf(intWinVal))) {

                        hmDisWinZsFnlVals.put(String.valueOf(intWinVal), String.valueOf(hmDisAvgZsVals.get(me.getKey())));

                    } else {

                        strTmp = hmDisWinZsFnlVals.get(String.valueOf(intWinVal));
                        hmDisWinZsFnlVals.remove(String.valueOf(intWinVal));
                        hmDisWinZsFnlVals.put(String.valueOf(intWinVal), strTmp + strSplit + String.valueOf(hmDisAvgZsVals.get(me.getKey())));

                    }
                }

                it = hmDisWinZsFnlVals.entrySet().iterator();
                Map.Entry<String, String> me2 = null;


                while (it.hasNext()) {

                    me2 = (Map.Entry<String, String>) it.next();
                    strTmp = me2.getValue();
                    strArr = strTmp.split(strSplit);
                    dblSum1 = 0;
                    intNodeNum = 0;

                    for (int i = 0; i < strArr.length; i++) {

                        dblSum1 += Double.valueOf(strArr[i]);
                        intNodeNum++;
                    }

                    bfw.write(me2.getKey() + strSplit + (dblSum1 / intNodeNum) + "\n");
                    System.out.println(me2.getKey() + strSplit + (dblSum1 / intNodeNum));
                }

                hmDisAvgZsVals.clear();
                hmDisWinZsFnlVals.clear();
                hmDisWinZsVals.clear();


            } else {

                System.out.println("Not considering processing yet!");
            }

            bfw.close();

            hmDisAvgVals.clear();
            hmDisWinVals.clear();

            System.out.println("Finishing the running at the time:" + new Date());


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /*
     *2012.11.21 zxz
     *该函数在node degree 数据文件的基础上实现对各节点的相应的树型结构根代码的计数。
     *@param strDisNodeDegreeFile 文件格式如dis_net_cv_ssymp_chisn_02_nd.txt，是generateNodeDegree函数产生的文件。
     **/
    public void genNodeDegreeTreeCatFreq(String strDisNodeDegreeFile, String strSplit, String strMeSHCodeFile,
                                         String strMeSHTreeFile, boolean blUsingMeshCode, String strRSFile) {


        BufferedReader bf;
        BufferedWriter bfw;
        String strTmp = "";

        String strLine = "";

        String[] strArr = null;

        int[] intFreqArr = new int[28]; //用于存储计数。

        HashMap<String, String> hmDisNodes = new HashMap<String, String>();

        try {


            bf = new BufferedReader(new FileReader(strDisNodeDegreeFile));

            if (blUsingMeshCode) {

                //This means that the disease node degree file has mesh codes.
                loadDisMeSH2HashMap(strMeSHCodeFile, strSplit);


            }

            loadDisCat2HashMap(strMeSHTreeFile, ";");

            while ((strLine = bf.readLine()) != null) {

                strArr = strLine.split(strSplit);

                if (blUsingMeshCode) {

                    strArr[1] = hmDisMeSHCodes.get(strArr[1]);

                }

                if (!hmDisNodes.containsKey(strArr[1])) {
                    hmDisNodes.put(strArr[1], strArr[3]);
                }


            }

            bf.close();

            strArr = hmDisNodes.keySet().toArray(new String[0]);

            String[] strTmpArr = null;

            for (int k = 0; k < strArr.length; k++) {

                strTmp = hmDisMeSHTreeCodes.get(strArr[k]);

                if (strTmp != null) {

                    strTmpArr = strTmp.split("/");

                    for (int m = 0; m < strTmpArr.length; m++) {

                        strTmp = strTmpArr[m].substring(0, 3);

                        if (strTmp.indexOf("C") == 0) {
                            intFreqArr[Integer.valueOf(strTmp.substring(1))] += Integer.valueOf(hmDisNodes.get(strArr[k]));
                        } else {
                            intFreqArr[27] += Integer.valueOf(hmDisNodes.get(strArr[k]));
                        }
                    }
                }
            }

            bfw = new BufferedWriter(new FileWriter(strRSFile, true));

            for (int i = 1; i < intFreqArr.length; i++) {

                if (i < 10) strTmp = "C0" + i;
                else if (i < 27) strTmp = "C" + i;
                else strTmp = "F03";

                bfw.write(strTmp + strSplit + intFreqArr[i] + "\n");

            }

            bfw.close();

            if (blUsingMeshCode) {
                hmDisMeSHCodes.clear();
            }
            hmDisMeSHTreeCodes.clear();

            System.out.println("Finished at the time:" + new Date());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 2012.8.13 zxz
     * This function is used to load the disease tree codes to hashmap.
     */

    private void loadDisCat2HashMap(String strMeSHTreeFile, String strSplit) {

        BufferedReader bf;
        String strTmp = "";

        String strLine = null;
        String[] strArr = null;

        int intTermCount = 0;

        try {

            bf = new BufferedReader(new FileReader(strMeSHTreeFile));

            while ((strLine = bf.readLine()) != null) {

                if (!strLine.equalsIgnoreCase("")) {

                    strArr = strLine.split(strSplit);

                    if (strArr[1].indexOf('C') == 0 ||
                            strArr[1].indexOf('F') == 0) {
                        // It is disease terms.

                        if (hmDisMeSHTreeCodes.containsKey(strArr[0])) {

                            strTmp = hmDisMeSHTreeCodes.get(strArr[0]);
                            hmDisMeSHTreeCodes.remove(strArr[0]);
                            hmDisMeSHTreeCodes.put(strArr[0], strTmp + "/" + strArr[1]);

                        } else {

                            hmDisMeSHTreeCodes.put(strArr[0], strArr[1]);
                            intTermCount++;

                        }

                    }
                }


            }

            bf.close();

            System.out.println("There are " + intTermCount + " terms loaded in hmDisMeSHTreeCodes HashMap!");
            System.out.println("The values of HashMap are seperated by '/'");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 2012.8.13 zxz
     * This function is used to load the disease MeSH codes to hashmap.
     *
     * @param strMeSHCodeFile like mesh_mh_ui4.txt
     */

    private void loadDisMeSH2HashMap(String strMeSHCodeFile, String strSplit) {

        BufferedReader bf;

        String strTmp = "";

        String strLine = null;
        String[] strArr = null;

        int intTermCount = 0;

        try {

            bf = new BufferedReader(new FileReader(strMeSHCodeFile));

            bf.readLine(); // ignore the first line.

            while ((strLine = bf.readLine()) != null) {

                strArr = strLine.split(strSplit);
                hmDisMeSHCodes.put(strArr[0], strArr[2]);
                intTermCount++;


            }

            bf.close();

            System.out.println("There are " + intTermCount + " terms loaded in hmDisMeSHCodes HashMap!");


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        NodeDiversity nd = new NodeDiversity();

//		PPI
        // If the code is not running successfully, please change this to an absolute path.
        String strPre = "./SCN";
        String strFile_ppi=strPre+"ppi/Protein-protein interaction network.csv";
        String strFileObj_ppi=strPre+"ppi/ppi_degree.txt";
		nd.generateNodeDegree(strFile_ppi, strFileObj_ppi,",", 0, 1);
		String strFileObj_ppi_NB1=strPre+"NodeDiversity/PPI_NodeDiversity.txt";
		nd.getBridgeCoefficient_m(strFileObj_ppi, strFileObj_ppi_NB1, true);

		//SCN
        String strFile_SCN=strPre+"/SCN/tmp/Symptom clinical association network.csv";
        String strFileObj_SCN=strPre+"/SCN/tmp/SCN_degree.txt";
        nd.generateNodeDegree(strFile_SCN, strFileObj_SCN,",", 0, 1);
        String strFileObj_SCN_NB1=strPre+"NodeDiversity/SCN_NodeDiversity.txt";
        nd.getBridgeCoefficient_m(strFileObj_SCN, strFileObj_SCN_NB1, true);

    }
}
