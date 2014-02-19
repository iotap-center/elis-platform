#!/bin/bash
HOST="ftp.elvaco.se"
USER="eonimd"
PASS="Metering2013%"
FTPURL="ftp://$USER:$PASS@$HOST"
LCD="/tmp/mkb-water-data"
RCD="/"
#DELETE="--delete"
lftp -c "open '$FTPURL';
lcd $LCD;
cd $RCD;
mirror --verbose"

# get rid of dups 
cd /tmp/mkb-water-data
sort export_*.txt | uniq > all.txt

