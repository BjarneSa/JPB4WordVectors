import unittest
import json
import requests

import pybridgeApplication


# The class provides different test cases for pybridge with word vector model.
class TestApplication(unittest.TestCase):

    def test_get_vector_king(self):
        """
        Test case for word vector to input 'king'. Because of being a valid request, server status code should be 200
        and the three selected values should be part of the vector.

        :return:
        """
        request_king = requests.get("http://localhost/getVector/king")
        self.assertEqual(request_king.status_code, 200)
        data = json.loads(request_king.content)
        first_item_in = False
        middle_item_in = False
        last_item_in = False
        for element in data['vector']:
            if element == 0.10819999873638153:
                first_item_in = True
            elif element == -0.08529999852180481:
                middle_item_in = True
            elif element == 0.025599999353289604:
                last_item_in = True
        self.assertTrue(first_item_in)
        self.assertTrue(middle_item_in)
        self.assertTrue(last_item_in)

    #
    def test_get_vector_queen(self):
        """
        Test case for word vector to input 'queen'. Because of being a valid request, server status code should be
        200 and the three selected values should be part of the vector.

        :return:
        """
        request_queen = requests.get("http://localhost/getVector/queen")
        self.assertEqual(request_queen.status_code, 200)
        data = json.loads(request_queen.content)
        first_item_in = False
        middle_item_in = False
        last_item_in = False
        for element in data['vector']:
            if element == 0.21580000221729279:
                first_item_in = True
            elif element == -0.07490000128746033:
                middle_item_in = True
            elif element == 0.042399998754262924:
                last_item_in = True
        self.assertTrue(first_item_in)
        self.assertTrue(middle_item_in)
        self.assertTrue(last_item_in)

    def test_get_vector_three_dots(self):
        """
        Test case for word vector to input '...'. Because of being a valid request, server status code should be 200
        and the three selected values should be part of the vector.

        :return:
        """
        request_dots = requests.get("http://localhost/getVector/...")
        self.assertEqual(request_dots.status_code, 200)
        data = json.loads(request_dots.content)
        first_item_in = False
        middle_item_in = False
        last_item_in = False
        for element in data['vector']:
            if element == -0.006899999920278788:
                first_item_in = True
            elif element == 0.05640000104904175:
                middle_item_in = True
            elif element == -0.07680000364780426:
                last_item_in = True
        self.assertTrue(first_item_in)
        self.assertTrue(middle_item_in)
        self.assertTrue(last_item_in)

    def test_get_vector_aterisk(self):
        """
        Test case for word vector to input '*'. Because of being a valid request, server status code should be 200
        and the three selected values should be part of the vector.

        :return:
        """
        request_string = "*"
        request_aterisk = requests.get("http://localhost/getVector/" + request_string)
        self.assertEqual(request_aterisk.status_code, 200)
        data = json.loads(request_aterisk.content)
        first_item_in = False
        middle_item_in = False
        last_item_in = False
        for element in data['vector']:
            if element == 0.06859999895095825:
                first_item_in = True
            elif element == 0.031099999323487282:
                middle_item_in = True
            elif element == -0.07559999823570251:
                last_item_in = True
        self.assertTrue(first_item_in)
        self.assertTrue(middle_item_in)
        self.assertTrue(last_item_in)

    def test_get_vector_number(self):
        """
        Test case for word vector to a number as input. Because of being a valid request, server status code should
        be 200 and the three selected values should be part of the vector.

        :return:
        """
        request_number = 42
        request_only_number = requests.get("http://localhost/getVector/" + str(request_number))
        self.assertEqual(request_only_number.status_code, 200)
        data = json.loads(request_only_number.content)
        first_item_in = False
        middle_item_in = False
        last_item_in = False
        for element in data['vector']:
            if element == -0.02710000053048134:
                first_item_in = True
            elif element == 0.22010000050067902:
                middle_item_in = True
            elif element == 0.05820000171661377:
                last_item_in = True
        self.assertTrue(first_item_in)
        self.assertTrue(middle_item_in)
        self.assertTrue(last_item_in)

    def test_get_vector_stringAndNumber(self):
        """
        Test case for word vector to input of combined word and number. Because of being a invalid request,
        server status code should be 500 representing an internal server error (KeyError).

        :return:
        """
        request_string = "hello42"
        request_string_number = requests.get("http://localhost/getVector/" + request_string)
        self.assertEqual(request_string_number.status_code, 500)

    def test_get_vector_special_characters(self):
        """
        Test case for word vector to input are multiple special characters. Because of being a invalid request,
        server status code should be 500 representing an internal server error (KeyError).

        :return:
        """
        request_string = "**"
        request_special_characters = requests.get("http://localhost/getVector/" + request_string)
        self.assertEqual(request_special_characters.status_code, 500)

    def test_get_vector_unknown_word(self):
        """
        Test case for word vector to input is not an English word. Because of being a invalid request,
        server status code should be 500 representing an internal server error (KeyError).

        :return:
        """
        request_string = "qwert"
        request_unkown_word = requests.get("http://localhost/getVector/" + request_string)
        self.assertEqual(request_unkown_word.status_code, 500)

    def test_get_vector_large_number(self):
        """
        Test case for word vector to a large number as input. Because of being a valid request, server status code
        should be 200 and the three selected values should be part of the vector.

        :return:
        """
        request_number = 10000
        request_large_number = requests.get("http://localhost/getVector/" + str(request_number))
        self.assertEqual(request_large_number.status_code, 200)
        data = json.loads(request_large_number.content)
        first_item_in = False
        middle_item_in = False
        last_item_in = False
        for element in data['vector']:
            if element == 0.019500000402331352:
                first_item_in = True
            elif element == 0.050700001418590546:
                middle_item_in = True
            elif element == -0.003599999938160181:
                last_item_in = True
        self.assertTrue(first_item_in)
        self.assertTrue(middle_item_in)
        self.assertTrue(last_item_in)

    def test_get_vector_too_large_number(self):
        """
        Test case for word vector to input is a too large/ complex number. Because of being a invalid
        request, server status code should be 500 representing an internal server error (KeyError).

        :return:
        """
        request_number = 7871389
        request_too_large_number = requests.get("http://localhost/getVector/" + str(request_number))
        self.assertEqual(request_too_large_number.status_code, 500)


###########################################################################################################


if __name__ == '__main__':
    unittest.main()
