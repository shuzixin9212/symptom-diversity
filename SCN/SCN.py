from collections import defaultdict
import pandas as pd
import numpy as np
import networkx as nx


def SCN_build():
    """
    We curated the data related to clinical symptoms derived from a well-recognized textbook named
    differential diagnosis of traditional Chinese medicine symptoms (DDTS)13 for clinicians in China.
    The Symptoms Clinical Network(SCN) is constructed from the data of the relationship
    between symptoms and symptoms.
    :return:
    """
    data = pd.read_excel('SCN/Input/Source data for construct symptom clinical association network.xlsx')
    data = list(data['Symptom cluster'])
    SCNNet = []
    SCN_edge_fre = {}
    SCN_node_fre = defaultdict(list)
    for str in data:
        temp = list(set(str.split('|')))
        [SCN_node_fre[node].append(1) for node in temp]
        for i in range(0, len(temp)):
            for j in range(i+1, len(temp)):
                edge_index = '|'.join([temp[i], temp[j]])
                if [temp[i], temp[j]] not in SCNNet and [temp[j], temp[i]] not in SCNNet:
                    SCNNet.append([temp[i], temp[j]])
                    SCN_edge_fre.setdefault(edge_index, 1)
                else:
                    index = edge_index if edge_index in SCN_edge_fre.keys() else '|'.join([temp[j], temp[i]])
                    SCN_edge_fre[index] += 1
    SCNNet_node_out = [[node, len(SCN_node_fre[node])] for node in SCN_node_fre.keys()]
    SCNNet_edge_out = [[edge.split('|')[0], edge.split('|')[1], SCN_edge_fre[edge]] for edge in SCN_edge_fre.keys()]
    SCNNet_node_out = sorted(SCNNet_node_out, key=(lambda x: x[1]), reverse=True)
    SCNNet_edge_out = sorted(SCNNet_edge_out, key=(lambda x: x[2]), reverse=True)
    writer = pd.ExcelWriter('SCN/tmp/Symptom clinical association network with frequency.xlsx', engine='openpyxl')
    pd.DataFrame(SCNNet_node_out, columns=['Symptom', 'Frequency']).to_excel(excel_writer=writer, sheet_name='Node', index=False)
    pd.DataFrame(SCNNet_edge_out, columns=['Source', 'Target', 'Frequency']).to_excel(excel_writer=writer, sheet_name='Relationship', index=False)
    writer.save()
    writer.close()
    pd.DataFrame(SCNNet, columns=['Source', 'Target']).to_csv('SCN/tmp/Symptom clinical association network.csv', index=False, encoding='utf-8')


def cal_coef(network_path, node_diversity_path, out_path, columnList=[]):
    net = pd.read_csv(network_path)
    node_diversity = {}
    with open(node_diversity_path, 'r', encoding='utf-8') as f:
        for row in f.readlines():
            temp = row.strip('\n').split('\t')
            node_diversity[temp[1]] = temp[2]

    f.close()

    G = nx.from_pandas_edgelist(net, 'Source', 'Target')
    D = list(nx.degree(G))
    nodes_coef_list = []
    for nodes in D:
        nodes_coef_list.append([nodes[0], nodes[1], node_diversity[nodes[0]]])
    nodes_coef = pd.DataFrame(nodes_coef_list, columns=columnList)
    nodes_coef.to_csv(out_path, index=False, encoding='utf_8_sig')
    return nodes_coef


def symptoGene():
    """
    We use the following data:
    Symptom in SCN mapping to CUI symptom;
    341 CUI symptom with 3598 genes;
    the relevant indicators of genes in the PPI network.
    To get the genes corresponding to each symptom in the SCN network and
    the correlation coefficients in the molecular network.
    :return:
    """
    sym_CUI = pd.read_excel('SCN/Input/Symptom in SCN mapping to CUI symptom.xlsx')
    CUI_gene = pd.read_excel('SCN/Input/341 CUI symptom with 3598 genes.xlsx')
    gene_coef = pd.read_csv('ppi/gene_div_deg.csv')
    CUI_gene_coef = pd.merge(CUI_gene, gene_coef, on='gene')
    sym_gene_coef = pd.merge(CUI_gene_coef, sym_CUI, on='CUI code')
    sym_gene_coef.to_excel('SCN/tmp/sym_geneDiv.xlsx', index=False)


def cal_nodeDiv():
    """
    Here we count the following details of each symptom in SCN:
    the gene with the largest node diversity,
    the number of genes in the symptom corresponding to the PPI,
    and the largest node diversity and maximum degree of related genes.
    :return:
    """
    data = pd.read_excel('SCN/tmp/sym_geneDiv.xlsx')
    data.drop_duplicates(['Symptom in SCN', 'gene'], inplace=True)
    res = data.sort_values(by='diversity', ascending=False)\
        .groupby('Symptom in SCN', as_index=False)[['Symptom in SCN (English)', 'gene', 'degree', 'diversity']]\
        .agg({'Symptom in SCN (English)': ['first'], 'gene': ['first', 'count'], 'degree': ['max'], 'diversity': ['first']})
    res.columns = ['Symptom in SCN', 'Symptom English name', 'gene name', 'gene number', 'Molecular network node degree', 'Molecular network node diversity']
    res.to_excel('SCN/tmp/The manifestation of SCN symptoms in molecular networks.xlsx', index=False)


def merge():
    data1 = pd.read_excel('SCN/tmp/The manifestation of SCN symptoms in molecular networks.xlsx')
    data2 = pd.read_csv('SCN/tmp/SCN_nodes_coef.csv')
    data1.set_index('Symptom in SCN', inplace=True)
    data2.set_index('Symptom in SCN', inplace=True)
    data = pd.concat([data1, data2], axis=1, join='inner')
    data.to_excel('SCN/Correlation between PD and MGD of 116 symptoms.xlsx')


if __name__ == '__main__':
    SCN_build()
    cal_coef('SCN/tmp/Symptom clinical association network.csv', 'NodeDiversity/SCN_NodeDiversity.txt',
             'SCN/tmp/SCN_nodes_coef.csv', columnList=['Symptom in SCN', 'Phenotypic node degree in SCN',
                                                       'Phenotypic node diversity in SCN'])
    cal_coef('ppi/Protein-protein interaction network.csv', 'NodeDiversity/PPI_NodeDiversity.txt',
             'ppi/gene_div_deg.csv', columnList=['gene', 'degree', 'diversity'])
    symptoGene()
    cal_nodeDiv()
    merge()
