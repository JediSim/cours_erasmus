---
Knowledge Graph Engineering
---

# Knowledge Graph Engineering

## Overview

- **exam**
  - Project with intermediate report at the end of each iTelos phase
  - Final exam with a presentation of the project

## Introduction

Entity Graphs (EG): a graph where nodes are entities and edges are relations between entities
EType graphs (ETG): Use class to represent entities

Knowledge layer is the schema of the db.

### Ontology

- **ontology**: a formal specification of a shared conceptualization

## Reuse Problem

repository: like git hub. Just a place ton put data
catalogues: makes data searchable

## iTelos - A KGE Methodology

methodology defined to reduce effort of the dev, provide support to the user, produces reusable resources

### Phases 1 - Inception

- **inception**: collect data and produce a set of semi-formal data, with objective to extract, clean, format and model the informal resources

#### Input

- **The purpose**: use to make the right decisions over the issues of each phase. during inception use to **clarifying the relevance of data**
- **Data source list**: list of data sources to be used

#### Activities

- **heterogeneity of the sources**: create some different execution of this phase. As a consequence, an **iterative execution** over the source list is needed

##### - Scraping

- **scraping**: get some data on the web

##### - Data cleaning

- **data cleaning**: remove noise from the data

##### - Formatting

- **formatting**: convert the data into a standard format

##### - Schema modeling

Some times dataset's **information needs to be interpreted**

##### - KGC

- **KGC**: knowledge graph construction combines the data and the schema to create **a single object representing a semi-formal information model**

#### In practice

1. use and implementation of **data management lib** to **scrape** and **clean** the data
2. datasets schema **(Protègè)**
3. **KGC** with **Karma** tool
