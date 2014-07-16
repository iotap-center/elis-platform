#!/bin/bash

mkdir -p /home/felix/mkb-water-data

HOST="ftp.elvaco.se"
USER="eonimd"
PASS="Metering2013%"
FTPURL="ftp://$USER:$PASS@$HOST"
LCD="/home/felix/mkb-water-data"
RCD="/"
#DELETE="--delete"
lftp -c "open '$FTPURL';
lcd $LCD;
cd $RCD;
mirror --verbose"

# get rid of dups 
cd /home/felix/mkb-water-data
sort export_*.txt | uniq > all.txt

