#!/bin/bash

######################################################################################
#
# Test script for all OSSIM repositories. Required environment variables are:
#
#   OSSIM_DATA -- Local directory to contain elevation, imagery, and expected results
#
######################################################################################
#set -x; trap read debug

echo; echo; 
echo "################################################################################"
echo "#  Running `basename "$0"` out of <$PWD>"
echo "################################################################################"

# Set run-time environment:
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source $SCRIPT_DIR/ossim-env.sh
if [ $? != 0 ] ; then 
  echo "ERROR: Could not set OBT environment.";
  echo; exit 1;
fi

# Check for required environment:
if [ ! -d $OSSIM_DATA ] || [ ! -d $OSSIM_BATCH_TEST_DATA ] || [ -z $OSSIM_BATCH_TEST_RESULTS ]; then
  echo "ERROR: Environment variables not correct. Check the following paths for problems:"
  echo "   OSSIM_DATA = <$OSSIM_DATA> ";
  echo "   OSSIM_BATCH_TEST_DATA = <$OSSIM_BATCH_TEST_DATA> ";
  echo "   OSSIM_BATCH_TEST_RESULTS = <$OSSIM_BATCH_TEST_RESULTS> ";
  echo; exit 1;
fi

# Make sure the output directories are created:
if [ ! -d $OSSIM_BATCH_TEST_RESULTS ]; then
  echo "STATUS: Creating directory <$OSSIM_BATCH_TEST_RESULTS> to hold test output.";
  mkdir -p $OSSIM_BATCH_TEST_RESULTS;
fi
if [ ! -d $OSSIM_BATCH_TEST_RESULTS/out ]; then
  echo "STATUS: Creating directory <$OSSIM_BATCH_TEST_RESULTS/out> to hold test results.";
  mkdir -p $OSSIM_BATCH_TEST_RESULTS/out;
fi
if [ ! -d $OSSIM_BATCH_TEST_RESULTS/log ]; then
  echo "STATUS: Creating directory <$OSSIM_BATCH_TEST_RESULTS/log> to hold test logs.";
  mkdir -p $OSSIM_BATCH_TEST_RESULTS/log;
fi

# TEST 1: Check ossim-info version:
echo; echo "STATUS: Running ossim-info --config test...";
COMMAND1="ossim-info --config --plugins"
$COMMAND1
if [ $? -ne 0 ]; then
  echo; echo "ERROR: Failed while attempting to run <$COMMAND1>."
  echo; exit 1;
fi
echo "STATUS: Passed ossim-info --config test.";

echo; echo "STATUS: Running ossim-info --version test...";
ossim-info --version
COUNT="$(ossim-info --version | grep --count 'version: 1.9')"
echo "COUNT = <$COUNT>"
if [ $COUNT != "1" ]; then
  echo "FAIL: Failed ossim-info --version test"; 
  echo; exit 1;
fi
echo "STATUS: Passed ossim-info --version test.";

# Sync test data against S3:
if [ -z $S3_DATA_BUCKET ]; then
  echo "ERROR: No URL specified for S3 bucket containing test data. Expecting S3_DATA_BUCKET environment variable."
  echo; exit 1;
fi
aws s3 sync $S3_DATA_BUCKET/Batch_test_data $OSSIM_BATCH_TEST_DATA
aws s3 sync $S3_DATA_BUCKET/Batch_test_expected $OSSIM_BATCH_TEST_EXPECTED
aws s3 sync $S3_DATA_BUCKET/elevation $OSSIM_DATA/elevation

# Run batch tests
pushd $OSSIM_DEV_HOME/ossim-ci/batch_tests;
echo; echo "STATUS: Running batch tests in <$PWD>..."
ossim-batch-test super-test.kwl
EXIT_CODE=$?
popd
echo "STATUS: ossim-batch-test EXIT_CODE = $EXIT_CODE"
if [ $EXIT_CODE != 0 ]; then
  echo "FAIL: Failed batch test"
  echo; exit 1;
fi

# Success!
echo "STATUS: Passed all tests."
echo
exit 0

